package com.nunop.rickandmorty.datasource.remotedatasource

import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import retrofit2.Response

interface LocationRemoteDataSource {

    suspend fun getLocations(page: Int): Response<LocationResponse>
}