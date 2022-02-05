package com.nunop.rickandmorty.repository.location

import android.content.Context
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Location
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface LocationRepository {
    suspend fun getAllLocations(): Flow<PagingData<ResultLocation>>
    fun getAllLocationsDao(): Flow<List<Location>>
    suspend fun getLocations(pageNumber: Int): Response<LocationResponse>
    suspend fun insertLocation(location: Location)

    suspend fun getLocationById(locationId: Int, context: Context?): Location?
}