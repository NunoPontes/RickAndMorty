package com.nunop.rickandmorty.ui.character.characterDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunop.rickandmorty.repository.character.CharacterRepository

class CharacterDetailsViewModelProviderFactory(private val repository: CharacterRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterDetailsViewModel(repository) as T
    }
}