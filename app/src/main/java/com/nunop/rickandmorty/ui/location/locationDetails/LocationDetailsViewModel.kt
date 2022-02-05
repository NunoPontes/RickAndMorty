package com.nunop.rickandmorty.ui.location.locationDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nunop.rickandmorty.App
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.repository.location.LocationRepository
import com.nunop.rickandmorty.utils.Resource
import kotlinx.coroutines.launch

class LocationDetailsViewModel(app: Application, private val repository: LocationRepository) :
    AndroidViewModel(app) {

    private var location: MutableLiveData<Resource<Location>> = MutableLiveData()
    var locationLiveData: LiveData<Resource<Location>> = location

    fun getLocationById(locationId: Int) {
        viewModelScope.launch {
            location.postValue(Resource.Loading())
            val result = repository.getLocationById(locationId, getApplication<App>())
            if (result != null) {
                location.postValue(Resource.Success(result))
            } else {
                location.postValue(Resource.Error("Error")) //Todo: extract errors
            }
        }
    }
}