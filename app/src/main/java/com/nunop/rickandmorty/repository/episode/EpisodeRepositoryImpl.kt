package com.nunop.rickandmorty.repository.episode

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.paging.EpisodeRemoteMediator
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Constants.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class EpisodeRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
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
            remoteMediator = EpisodeRemoteMediator(
                remoteDataSource,
                localDataSource
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}