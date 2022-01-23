package com.nunop.rickandmorty.repository.episode

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.api.RickAndMortyAPI
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.paging.EpisodeRemoteMediator
import com.nunop.rickandmorty.utils.Constants.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class EpisodeRepositoryImpl(
    private val api: RickAndMortyAPI,
    private val db: Database
) : EpisodeRepository {

    @ExperimentalPagingApi
    override fun getEpisodesFromMediator(): Flow<PagingData<Episode>> {
        val pagingSourceFactory = { db.episodeDao.getEpisodesPaged() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false,
            ),
            remoteMediator = EpisodeRemoteMediator(
                api,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}