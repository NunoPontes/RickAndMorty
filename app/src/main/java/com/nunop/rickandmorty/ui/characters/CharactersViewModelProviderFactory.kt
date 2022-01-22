package com.nunop.rickandmorty.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunop.rickandmorty.repository.character.CharacterRepository

class CharactersViewModelProviderFactory(private val repository: CharacterRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharactersViewModel(repository) as T
    }
}