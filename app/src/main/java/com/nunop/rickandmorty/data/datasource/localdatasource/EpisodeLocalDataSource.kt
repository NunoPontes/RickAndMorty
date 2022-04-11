package com.nunop.rickandmorty.data.datasource.localdatasource

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.database.entities.relations.EpisodeCharacterCrossRef

interface EpisodeLocalDataSource {

    suspend fun insertEpisode(episode: Episode)

    suspend fun insertAllEpisodes(episodes: List<Episode>)

    suspend fun getEpisodeById(id: Int): Episode?

    fun getEpisodes(): LiveData<List<Episode>>

    fun getEpisodesPaged(): PagingSource<Int, Episode>

    suspend fun deleteAllEpisodes()

    suspend fun insertAllEpisodeCharacterCrossRef(
        episodeCharacterCrossRef:
        List<EpisodeCharacterCrossRef>
    )
}