package com.nunop.rickandmorty.data.datasource.remotedatasource

import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import retrofit2.Response

interface LocationRemoteDataSource {

    suspend fun getLocations(page: Int): Response<LocationResponse>

    suspend fun getLocationById(locationId: Int): Response<ResultLocation>
}