package com.nunop.rickandmorty.data.api

import com.nunop.rickandmorty.data.api.models.character.CharacterResponse
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.api.models.episode.EpisodeResponse
import com.nunop.rickandmorty.data.api.models.episode.ResultEpisode
import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterResponse>

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") characterId: Int
    ): Response<ResultCharacter>

    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int
    ): Response<LocationResponse>

    @GET("location/{id}")
    suspend fun getLocationById(
        @Path("id") locationId: Int
    ): Response<ResultLocation>

    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): Response<EpisodeResponse>

    @GET("episode/{id}")
    suspend fun getEpisodeById(
        @Path("id") episodeId: Int
    ): Response<ResultEpisode>
}