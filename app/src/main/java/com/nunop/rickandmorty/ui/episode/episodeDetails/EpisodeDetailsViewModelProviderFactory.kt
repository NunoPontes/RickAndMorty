package com.nunop.rickandmorty.ui.episode.episodeDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunop.rickandmorty.repository.episode.EpisodeRepository

class EpisodeDetailsViewModelProviderFactory(
    val app: Application,
    private val repository: EpisodeRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodeDetailsViewModel(app, repository) as T
    }
}