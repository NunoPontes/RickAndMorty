package com.nunop.rickandmorty.ui.location.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunop.rickandmorty.repository.location.LocationRepository

class LocationsViewModelProviderFactory(private val repository: LocationRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationsViewModel(repository) as T
    }
}