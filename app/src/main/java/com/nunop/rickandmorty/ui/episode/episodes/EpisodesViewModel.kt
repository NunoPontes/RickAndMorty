package com.nunop.rickandmorty.ui.episode.episodes

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nunop.rickandmorty.base.BaseViewModel
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.repository.episode.EpisodeRepository
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class EpisodesViewModel(
    private val repository: EpisodeRepository
) : BaseViewModel() {

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
}