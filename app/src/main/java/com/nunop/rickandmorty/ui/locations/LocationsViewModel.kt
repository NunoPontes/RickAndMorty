package com.nunop.rickandmorty.ui.locations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.repository.location.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocationsViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    val allLocations = repository.getAllLocationsDao().asLiveData()
    private lateinit var _locationsFlow: Flow<PagingData<ResultLocation>>
    val locationsFlow: Flow<PagingData<ResultLocation>>
        get() = _locationsFlow
    init {
        getAllLocations()
    }

    private fun getAllLocations() = launchPagingAsync({
        repository.getAllLocations().cachedIn(viewModelScope)
    }, {
        _locationsFlow = it
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
                Log.e("AAAAA", ex.toString())
            }
        }
    }
}