package com.nunop.rickandmorty.repository.character

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.database.entities.Character
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CharacterRepository {
    @ExperimentalPagingApi
    fun getCharactersFromMediator(): Flow<PagingData<Character>>

    suspend fun getCharacterByIdApi(characterId: Int): Response<ResultCharacter>

    suspend fun getCharacterByIdDb(characterId: Int): Character?

    suspend fun getCharacterById(characterId: Int): Character?
}