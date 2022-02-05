package com.nunop.rickandmorty.repository.location

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.paging.LocationsPagingDataSource
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.toEpisode
import com.nunop.rickandmorty.utils.toLocation
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val locationsPagingDataSource: LocationsPagingDataSource,
    private val remoteDataSource: RemoteDataSource
) : LocationRepository {

    override suspend fun getLocations(pageNumber: Int) =
        RetrofitInstance.api.getLocations(pageNumber)

    override fun getAllLocationsDao() = localDataSource.getLocations()

    override suspend fun insertLocation(location: Location) =
        localDataSource.insertLocation(location)

    override suspend fun getAllLocations(): Flow<PagingData<ResultLocation>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { locationsPagingDataSource }
    ).flow


    override suspend fun getLocationById(locationId: Int, context: Context?): Location? {
        val utilities = Utilities()
        if (context?.let { utilities.hasInternetConnection(it) } == true) {
            val response = remoteDataSource.getLocationById(locationId)
            if (response.isSuccessful && response.body() != null) {
                val locationResponse = response.body()
                locationResponse?.toLocation()?.let {
                    localDataSource.insertLocation(
                        it
                    )
                    return it
                }
                return null
            } else {
                return localDataSource.getLocationById(locationId)
            }
        } else {
            return localDataSource.getLocationById(locationId)
        }
    }
}