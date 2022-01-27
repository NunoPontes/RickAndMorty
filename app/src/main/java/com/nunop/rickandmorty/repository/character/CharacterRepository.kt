package com.nunop.rickandmorty.repository.character

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.database.entities.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    @ExperimentalPagingApi
    fun getCharactersFromMediator(): Flow<PagingData<Character>>
}