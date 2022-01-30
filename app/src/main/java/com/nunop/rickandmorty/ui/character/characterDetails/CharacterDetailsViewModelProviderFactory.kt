package com.nunop.rickandmorty.ui.character.characterDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunop.rickandmorty.repository.character.CharacterRepository

class CharacterDetailsViewModelProviderFactory(
    val app: Application,
    private val repository: CharacterRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterDetailsViewModel(app, repository) as T
    }
}