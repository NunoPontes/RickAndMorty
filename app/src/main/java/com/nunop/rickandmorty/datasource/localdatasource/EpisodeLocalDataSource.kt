package com.nunop.rickandmorty.datasource.localdatasource

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.nunop.rickandmorty.data.database.entities.Episode

interface EpisodeLocalDataSource {

    suspend fun insertEpisode(episode: Episode)

    suspend fun insertAllEpisodes(episodes: List<Episode>)

    suspend fun getEpisodeById(id: Int): Episode?

    fun getEpisodes(): LiveData<List<Episode>>

    fun getEpisodesPaged(): PagingSource<Int, Episode>

    suspend fun deleteAllEpisodes()
}