package com.nunop.rickandmorty.datasource.remotedatasource

import com.nunop.rickandmorty.api.RickAndMortyAPI
import com.nunop.rickandmorty.data.api.models.character.CharacterResponse
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.api.models.episode.EpisodeResponse
import com.nunop.rickandmorty.data.api.models.episode.ResultEpisode
import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import retrofit2.Response

class RemoteDataSource(private val api: RickAndMortyAPI) :
    CharacterRemoteDataSource,
    LocationRemoteDataSource,
    EpisodeRemoteDataSource {

    override suspend fun getCharacters(page: Int): Response<CharacterResponse> {
        return api.getCharacters(page)
    }

    override suspend fun getCharacterById(characterId: Int): Response<ResultCharacter> {
        return api.getCharacterById(characterId)
    }

    override suspend fun getLocations(page: Int): Response<LocationResponse> {
        return api.getLocations(page)
    }

    override suspend fun getLocationById(locationId: Int): Response<ResultLocation> {
        return api.getLocationById(locationId)
    }

    override suspend fun getEpisodes(page: Int): Response<EpisodeResponse> {
        return api.getEpisodes(page)
    }

    override suspend fun getEpisodeById(episodeId: Int): Response<ResultEpisode> {
        return api.getEpisodeById(episodeId)
    }
}