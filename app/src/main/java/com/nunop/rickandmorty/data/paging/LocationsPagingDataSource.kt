package com.nunop.rickandmorty.data.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.toLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationsPagingDataSource(private val remoteDataSource: RemoteDataSource,
                                private val localDataSource: LocalDataSource) :
    PagingSource<Int, ResultLocation>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultLocation> {
        val pageNumber = params.key ?: 1
        return try {
            val response = remoteDataSource.getLocations(pageNumber)
            val pagedResponse = response.body()
            val data = pagedResponse?.results

            var nextPageNumber: Int? = null
            pagedResponse?.info?.next?.let {
                val uri = Uri.parse(it)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
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