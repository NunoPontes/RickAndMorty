package com.nunop.rickandmorty.ui.character.characters

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nunop.rickandmorty.base.BaseViewModel
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.utils.Resource
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class CharactersViewModel(
    private val repository: CharacterRepository
) : BaseViewModel() {

    private lateinit var _charactersFlow: Resource<Flow<PagingData<Character>>>
    val charactersFlow: Resource<Flow<PagingData<Character>>>
        get() = _charactersFlow

    init {
        getAllCharacters()
    }

    private fun getAllCharacters() = launchPagingAsync({
        repository.getCharactersFromMediator().cachedIn(viewModelScope)
    }, {
        _charactersFlow = Resource.Success(it) //TODO: add verification for possible error, not
    // only success
    })
}