package com.nunop.rickandmorty.data.datasource.remotedatasource

import com.nunop.rickandmorty.data.api.models.character.CharacterResponse
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import retrofit2.Response

interface CharacterRemoteDataSource {

    suspend fun getCharacters(page: Int): Response<CharacterResponse>

    suspend fun getCharacterById(characterId: Int): Response<ResultCharacter>
}