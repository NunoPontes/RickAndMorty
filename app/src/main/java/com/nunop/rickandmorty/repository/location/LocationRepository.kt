package com.nunop.rickandmorty.repository.location

import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface LocationRepository {
    suspend fun getAllLocations(): Flow<PagingData<ResultLocation>>
    suspend fun getLocations(pageNumber: Int): Response<LocationResponse>
    suspend fun insertLocation(location: Location)

    suspend fun getLocationById(locationId: Int): Resource<Location?>
}