package com.nunop.rickandmorty.ui.character.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.repository.character.CharacterRepository

@ExperimentalPagingApi
class CharactersViewModelProviderFactory(private val repository: CharacterRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharactersViewModel(repository) as T
    }
}