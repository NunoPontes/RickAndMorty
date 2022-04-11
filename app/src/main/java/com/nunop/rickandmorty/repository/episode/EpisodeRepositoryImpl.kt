package com.nunop.rickandmorty.repository.episode

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.paging.EpisodeRemoteMediator
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.data.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Constants.Companion.PAGE_SIZE
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.toEpisode
import com.nunop.rickandmorty.utils.toEpisodeCharacterCrossRefList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class EpisodeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val context: Context?,
    private val episodeRemoteMediator: EpisodeRemoteMediator
) : EpisodeRepository {

    @ExperimentalPagingApi
    override fun getEpisodesFromMediator(): Flow<PagingData<Episode>> {
        val pagingSourceFactory = { localDataSource.getEpisodesPaged() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false,
            ),
            remoteMediator = episodeRemoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getEpisodeById(episodeId: Int): Episode? {
        val utilities = Utilities()
        if (context?.let { utilities.hasInternetConnection(it) } == true) {
            val response = remoteDataSource.getEpisodeById(episodeId)
            if (response.isSuccessful && response.body() != null) {
                val episodeResponse = response.body()
                episodeResponse?.toEpisode()?.let {
                    localDataSource.insertEpisode(
                        it
                    )
                    localDataSource.insertAllEpisodeCharacterCrossRef(episodeResponse.toEpisodeCharacterCrossRefList())
                    return it
                }
                return null
            } else {
                return localDataSource.getEpisodeById(episodeId)
            }
        } else {
            return localDataSource.getEpisodeById(episodeId)
        }
    }
}