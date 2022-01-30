package com.nunop.rickandmorty.datasource.remotedatasource

import com.nunop.rickandmorty.data.api.models.episode.EpisodeResponse
import retrofit2.Response

interface EpisodeRemoteDataSource {

    suspend fun getEpisodes(page: Int): Response<EpisodeResponse>
}