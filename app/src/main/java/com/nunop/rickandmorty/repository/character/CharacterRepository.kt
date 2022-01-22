package com.nunop.rickandmorty.repository.character

import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.character.CharacterResponse
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.database.entities.Character
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CharacterRepository {
    suspend fun getAllCharacters(): Flow<PagingData<ResultCharacter>>
    fun getAllCharactersDao(): Flow<List<Character>>
    suspend fun getCharacters(pageNumber: Int): Response<CharacterResponse>
    suspend fun insertCharacter(character: Character)
}