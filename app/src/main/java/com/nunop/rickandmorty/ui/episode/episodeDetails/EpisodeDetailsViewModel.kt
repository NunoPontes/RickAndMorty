package com.nunop.rickandmorty.ui.episode.episodeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nunop.rickandmorty.base.BaseViewModel
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.repository.episode.EpisodeRepository
import com.nunop.rickandmorty.utils.Error
import com.nunop.rickandmorty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(private val repository: EpisodeRepository) :
    BaseViewModel() {

    private var episode: MutableLiveData<Resource<Episode>> = MutableLiveData()
    var episodeLiveData: LiveData<Resource<Episode>> = episode

    fun getEpisodeById(episodeId: Int) {
        viewModelScope.launch {
            episode.postValue(Resource.Loading())
            val result = repository.getEpisodeById(episodeId)
            if (result != null) {
                episode.postValue(Resource.Success(result))
            } else {
                episode.postValue(Resource.Error(Error.GENERIC.error))
            }
        }
    }
}