package com.nunop.rickandmorty.ui.episode.episodeDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nunop.rickandmorty.App
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.repository.episode.EpisodeRepository
import com.nunop.rickandmorty.utils.Resource
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(app: Application, private val repository: EpisodeRepository) :
    AndroidViewModel(app) {

    private var episode: MutableLiveData<Resource<Episode>> = MutableLiveData()
    var episodeLiveData: LiveData<Resource<Episode>> = episode

    fun getEpisodeById(episodeId: Int) {
        viewModelScope.launch {
            episode.postValue(Resource.Loading())
            val result = repository.getEpisodeById(episodeId, getApplication<App>())
            if (result != null) {
                episode.postValue(Resource.Success(result))
            } else {
                episode.postValue(Resource.Error("Error")) //Todo: extract errors
            }
        }
    }
}