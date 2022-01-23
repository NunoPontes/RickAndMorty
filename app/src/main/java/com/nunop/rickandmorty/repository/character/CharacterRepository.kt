package com.nunop.rickandmorty.repository.character

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.character.CharacterResponse
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Episode
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CharacterRepository {
    @ExperimentalPagingApi
    fun getCharactersFromMediator(): Flow<PagingData<Character>>
}