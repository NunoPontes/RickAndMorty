package com.nunop.rickandmorty.ui.character.characterDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nunop.rickandmorty.App
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.utils.Error
import com.nunop.rickandmorty.utils.Resource
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(app: Application, private val repository: CharacterRepository) :
    AndroidViewModel(app) {

    private var character: MutableLiveData<Resource<Character>> = MutableLiveData()
    var characterLiveData: LiveData<Resource<Character>> = character

    fun getCharacterById(characterId: Int) {
        viewModelScope.launch {
            character.postValue(Resource.Loading())
            val result = repository.getCharacterById(characterId, getApplication<App>())
            if (result != null) {
                character.postValue(Resource.Success(result))
            } else {
                character.postValue(Resource.Error(Error.GENERIC.error))
            }
        }
    }
}