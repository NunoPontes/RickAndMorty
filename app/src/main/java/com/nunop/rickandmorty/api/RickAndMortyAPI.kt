package com.nunop.rickandmorty.api

import com.nunop.rickandmorty.data.api.models.character.CharacterResponse
import com.nunop.rickandmorty.data.api.models.episode.EpisodeResponse
import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterResponse>

    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int
    ): Response<LocationResponse>

    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): Response<EpisodeResponse>
}