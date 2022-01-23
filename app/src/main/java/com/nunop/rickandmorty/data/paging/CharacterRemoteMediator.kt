package com.nunop.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nunop.rickandmorty.api.RickAndMortyAPI
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.CharacterRemoteKey
import com.nunop.rickandmorty.utils.Constants.Companion.STARTING_PAGE_INDEX
import com.nunop.rickandmorty.utils.toListCharacters
import okio.IOException
import retrofit2.HttpException

@ExperimentalPagingApi
class CharacterRemoteMediator(
    private val api: RickAndMortyAPI,
    private val db: Database
) : RemoteMediator<Int, Character>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Character>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = api.getCharacters(page = page)
            val isEndOfList: Boolean = response.body()?.results?.isEmpty() == true
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.characterDao.deleteAll()
                    db.characterRemoteKeyDao.deleteAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.body()?.results?.map {
                    CharacterRemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { db.characterRemoteKeyDao.insertAll(it) }
                response.body()?.results?.toListCharacters()?.let { db.characterDao.insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Character>):
            CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.characterRemoteKeyDao.remoteKeysCharacterId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Character>): CharacterRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character ->
                character.id?.let {
                    db.characterRemoteKeyDao.remoteKeysCharacterId(
                        it
                    )
                }
            }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Character>): CharacterRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { cat -> cat.id?.let { db.characterRemoteKeyDao.remoteKeysCharacterId(it) } }
    }


}