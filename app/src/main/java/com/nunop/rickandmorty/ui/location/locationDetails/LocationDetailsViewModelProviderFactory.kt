package com.nunop.rickandmorty.ui.location.locationDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunop.rickandmorty.repository.location.LocationRepository

class LocationDetailsViewModelProviderFactory(
    val app: Application,
    private val repository: LocationRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationDetailsViewModel(app, repository) as T
    }
}