package com.nunop.rickandmorty.ui.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.repository.episode.EpisodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalPagingApi
class EpisodesViewModel(
    private val repository: EpisodeRepository
) : ViewModel() {

    private lateinit var _episodesFlow: Flow<PagingData<Episode>>
    val episodesFlow: Flow<PagingData<Episode>>
        get() = _episodesFlow

    init {
        getEpisodes()
    }

    private fun getEpisodes() = launchPagingAsync({
        repository.getEpisodesFromMediator().cachedIn(viewModelScope)
    }, {
        _episodesFlow = it
    })


    private inline fun <T> launchPagingAsync(
        crossinline execute: suspend () -> Flow<T>,
        crossinline onSuccess: (Flow<T>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = execute()
                onSuccess(result)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }
}