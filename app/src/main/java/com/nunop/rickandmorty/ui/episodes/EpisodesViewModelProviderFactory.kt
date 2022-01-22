package com.nunop.rickandmorty.ui.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.repository.episode.EpisodeRepository

@ExperimentalPagingApi
class EpisodesViewModelProviderFactory(private val repository: EpisodeRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodesViewModel(repository) as T
    }
}