package com.nunop.rickandmorty.repository.location

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.paging.LocationsPagingDataSource
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val dataSource: LocationsPagingDataSource
) : LocationRepository {

    override suspend fun getLocations(pageNumber: Int) =
        RetrofitInstance.api.getLocations(pageNumber)

    override fun getAllLocationsDao() = localDataSource.getLocations()

    override suspend fun insertLocation(location: Location) = localDataSource.insertLocation(location)

    override suspend fun getAllLocations(): Flow<PagingData<ResultLocation>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { dataSource }
    ).flow


}