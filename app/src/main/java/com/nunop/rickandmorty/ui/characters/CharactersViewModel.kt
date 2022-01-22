package com.nunop.rickandmorty.ui.characters

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.utils.toCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    val allCharacters = repository.getAllCharactersDao().asLiveData()
    private lateinit var _charactersFlow: Flow<PagingData<ResultCharacter>>
    val charactersFlow: Flow<PagingData<ResultCharacter>>
        get() = _charactersFlow
    init {
//        getCharactersCall()
        getAllCharacters()
    }

    private fun getAllCharacters() = launchPagingAsync({
        repository.getAllCharacters().cachedIn(viewModelScope)
    }, {
        _charactersFlow = it
    })

    private fun getCharactersCall() {
        viewModelScope.launch {
            val response = repository.getCharacters(1)
            if(response.isSuccessful){
                response.body()?.let {
                    it.results?.forEach { result->
                        repository.insertCharacter(result.toCharacter()) //TODO: map this object to db object
                    }
                }
            }
        }
    }

    private inline fun <T> launchPagingAsync(
        crossinline execute: suspend () -> Flow<T>,
        crossinline onSuccess: (Flow<T>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = execute()
                onSuccess(result)
            } catch (ex: Exception) {
                Log.e("AAAAA", ex.toString())
            }
        }
    }
}