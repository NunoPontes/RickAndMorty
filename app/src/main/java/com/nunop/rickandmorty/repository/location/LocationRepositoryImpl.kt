package com.nunop.rickandmorty.repository.location

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.paging.LocationsPagingDataSource
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.data.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.toLocation
import com.nunop.rickandmorty.utils.toLocationCharacterCrossRefList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val context: Context?
) : LocationRepository {

    override suspend fun getLocations(pageNumber: Int) =
        remoteDataSource.getLocations(pageNumber)

    override suspend fun insertLocation(location: Location) =
        localDataSource.insertLocation(location)

    override suspend fun getAllLocations(): Flow<PagingData<ResultLocation>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { LocationsPagingDataSource(remoteDataSource, localDataSource) }
        //it has to be a new instance to avoid crashing
    ).flow


    override suspend fun getLocationById(locationId: Int): Location? {
        val utilities = Utilities()
        if (context?.let { utilities.hasInternetConnection(it) } == true) {
            val response = remoteDataSource.getLocationById(locationId)
            if (response.isSuccessful && response.body() != null) {
                val locationResponse = response.body()
                locationResponse?.toLocation()?.let {
                    localDataSource.insertLocation(
                        it
                    )
                    localDataSource.insertAllLocationCharacterCrossRef(locationResponse.toLocationCharacterCrossRefList())
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