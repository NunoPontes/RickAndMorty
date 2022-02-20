package com.nunop.rickandmorty.data.paging

import androidx.paging.*
import androidx.room.withTransaction
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.CharacterRemoteKey
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Constants.Companion.STARTING_PAGE_INDEX
import com.nunop.rickandmorty.utils.Error
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.toListCharacters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import okio.IOException
import retrofit2.HttpException

@ExperimentalPagingApi
class CharacterRemoteMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : RemoteMediator<Int, Character>() {

    private val mutableFlowResults = MutableSharedFlow<Resource<Character?>>()

    val flowResults: Flow<Resource<Character?>>
        get() = mutableFlowResults

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
            val response = remoteDataSource.getCharacters(page = page)
            val isEndOfList: Boolean = response.body()?.results?.isEmpty() == true
            localDataSource.getDatabase().withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteAllCharacters()
                    localDataSource.deleteAllCharacterRemoteKey()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.body()?.results?.map {
                    CharacterRemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { localDataSource.insertAllCharacterRemoteKey(it) }
                response.body()?.results?.toListCharacters()
                    ?.let { localDataSource.insertAllCharacters(it) }
            }
            mutableFlowResults.emit(Resource.Success(null))
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            mutableFlowResults.emit(Resource.Error(Error.GENERIC.error))
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            mutableFlowResults.emit(Resource.Error(Error.GENERIC.error))
            return MediatorResult.Error(exception)
        } catch (exception: Exception){
            mutableFlowResults.emit(Resource.Error(Error.GENERIC.error))
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
                localDataSource.remoteKeysCharacterId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Character>): CharacterRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character ->
                character.id?.let {
                    localDataSource.remoteKeysCharacterId(
                        it
                    )
                }
            }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Character>): CharacterRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { cat -> cat.id?.let { localDataSource.remoteKeysCharacterId(it) } }
    }


}