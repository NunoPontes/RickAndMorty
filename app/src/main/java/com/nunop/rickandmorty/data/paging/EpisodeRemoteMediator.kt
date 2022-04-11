package com.nunop.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.database.entities.EpisodeRemoteKey
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.data.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Constants.Companion.STARTING_PAGE_INDEX
import com.nunop.rickandmorty.utils.toListEpisodes
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@ExperimentalPagingApi
class EpisodeRemoteMediator @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : RemoteMediator<Int, Episode>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Episode>
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
            val response = remoteDataSource.getEpisodes(page = page)
            val isEndOfList: Boolean = response.body()?.results?.isEmpty() == true
            localDataSource.getDatabase().withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteAllEpisodes()
                    localDataSource.deleteAllEpisodeRemoteKey()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.body()?.results?.map {
                    EpisodeRemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { localDataSource.insertAllEpisodeRemoteKey(it) }
                response.body()?.results?.toListEpisodes()
                    ?.let { localDataSource.insertAllEpisodes(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, Episode>
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

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Episode>): EpisodeRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                localDataSource.remoteKeysEpisodeId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Episode>): EpisodeRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { episode -> episode.id?.let { localDataSource.remoteKeysEpisodeId(it) } }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Episode>): EpisodeRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { cat -> cat.id?.let { localDataSource.remoteKeysEpisodeId(it) } }
    }


}