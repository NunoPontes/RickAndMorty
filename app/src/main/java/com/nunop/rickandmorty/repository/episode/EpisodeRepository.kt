package com.nunop.rickandmorty.repository.episode

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.database.entities.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    @ExperimentalPagingApi
    fun getEpisodesFromMediator(): Flow<PagingData<Episode>>

    suspend fun getEpisodeById(episodeId: Int): Episode?
}