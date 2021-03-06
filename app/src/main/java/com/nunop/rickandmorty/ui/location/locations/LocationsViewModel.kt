package com.nunop.rickandmorty.ui.location.locations

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nunop.rickandmorty.base.BaseViewModel
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: LocationRepository
) : BaseViewModel() {

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
}