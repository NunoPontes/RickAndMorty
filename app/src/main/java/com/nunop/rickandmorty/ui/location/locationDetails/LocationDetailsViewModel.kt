package com.nunop.rickandmorty.ui.location.locationDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.nunop.rickandmorty.base.BaseViewModel
import com.nunop.rickandmorty.repository.location.LocationRepository
import com.nunop.rickandmorty.repository.location.LocationState
import com.nunop.rickandmorty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val repository: LocationRepository
) : BaseViewModel() {

    var state by mutableStateOf(LocationState())

    fun getLocationById(locationId: Int) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            when (val result = repository.getLocationById(locationId)) {
                is Resource.Success -> {
                    state = state.copy(
                        location = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        location = null
                    )
                }
                else -> Unit
            }
        }
    }
}