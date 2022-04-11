package com.nunop.rickandmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.data.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.getNextLocationPage
import com.nunop.rickandmorty.utils.toLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationsPagingDataSource @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) :
    PagingSource<Int, ResultLocation>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultLocation> {
        val pageNumber: Int = params.key ?: 1
        return try {
            val response = remoteDataSource.getLocations(pageNumber)
            val pagedResponse = response.body()
            val data = pagedResponse?.results

            var nextPageNumber: Int? = null
            pagedResponse?.info?.next?.let {
                nextPageNumber = it.getNextLocationPage()
            }

            //Insert on the DB
            withContext(Dispatchers.IO) {
                data?.forEach {
                    localDataSource.insertLocation(it.toLocation())
                }
            }

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResultLocation>): Int =
        1
}