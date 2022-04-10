package com.nunop.rickandmorty.ui.character.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nunop.rickandmorty.base.BaseViewModel
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.utils.Error
import com.nunop.rickandmorty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(private val repository: CharacterRepository) :
    BaseViewModel() {

    private var character: MutableLiveData<Resource<Character>> = MutableLiveData()
    var characterLiveData: LiveData<Resource<Character>> = character

    fun getCharacterById(characterId: Int) {
        viewModelScope.launch {
            character.postValue(Resource.Loading())
            val result = repository.getCharacterById(characterId)
            if (result != null) {
                character.postValue(Resource.Success(result))
            } else {
                character.postValue(Resource.Error(Error.GENERIC.error))
            }
        }
    }
}