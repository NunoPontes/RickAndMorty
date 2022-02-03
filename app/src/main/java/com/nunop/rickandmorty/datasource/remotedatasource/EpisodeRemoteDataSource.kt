package com.nunop.rickandmorty.datasource.remotedatasource

import com.nunop.rickandmorty.data.api.models.episode.EpisodeResponse
import com.nunop.rickandmorty.data.api.models.episode.ResultEpisode
import retrofit2.Response

interface EpisodeRemoteDataSource {

    suspend fun getEpisodes(page: Int): Response<EpisodeResponse>

    suspend fun getEpisodeById(episodeId: Int): Response<ResultEpisode>
}