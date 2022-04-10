package com.nunop.rickandmorty.ui.location.locationDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nunop.rickandmorty.base.BaseViewModel
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.repository.location.LocationRepository
import com.nunop.rickandmorty.utils.Error
import com.nunop.rickandmorty.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val repository:
    LocationRepository
) :
    BaseViewModel() {

    private var location: MutableLiveData<Resource<Location>> = MutableLiveData()
    var locationLiveData: LiveData<Resource<Location>> = location

    fun getLocationById(locationId: Int) {
        viewModelScope.launch {
            location.postValue(Resource.Loading())
            val result = repository.getLocationById(locationId)
            if (result != null) {
                location.postValue(Resource.Success(result))
            } else {
                location.postValue(Resource.Error(Error.GENERIC.error))
            }
        }
    }
}