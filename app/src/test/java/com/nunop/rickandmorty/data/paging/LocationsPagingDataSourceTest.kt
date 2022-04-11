package com.nunop.rickandmorty.data.paging

import androidx.paging.PagingSource
import com.nunop.rickandmorty.data.api.models.Info
import com.nunop.rickandmorty.data.api.models.location.LocationResponse
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.data.datasource.remotedatasource.RemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class LocationsPagingDataSourceTest {

    private lateinit var locationsPagingDataSource: LocationsPagingDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        remoteDataSource = mock(RemoteDataSource::class.java)
        localDataSource = mock(LocalDataSource::class.java)
        locationsPagingDataSource = LocationsPagingDataSource(remoteDataSource, localDataSource)
    }

    @Test
    fun `Sends success result when all fields valid`() = runTest {
        val resultLocation = ResultLocation(
            created = "",
            dimension = "",
            id = 1,
            name = "aa",
            residents = listOf(""),
            type = "",
            url = ""
        )
        val location = Location(
            id = 1,
            name = "aa",
            type = "",
            created = "",
            dimension = "",
            url = ""
        )
        val listOfLocation = listOf(resultLocation)
        val locationResponse = LocationResponse(
            info = Info(
                count = null,
                next = "https://rickandmortyapi.com/api/location?page=2",
                pages = null,
                prev = null
            ),
            results = listOfLocation
        )

        val listResponse: Response<LocationResponse> =
            Response.success(locationResponse)

        doReturn(listResponse).`when`(remoteDataSource).getLocations(1)
        assertEquals(
            expected = PagingSource.LoadResult.Page(
                data = listOfLocation,
                prevKey = null,
                nextKey = 2
            ),
            actual = locationsPagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        )

        verify(remoteDataSource).getLocations(1)
        verify(localDataSource).insertLocation(location)

        verifyNoMoreInteractions(remoteDataSource)
        verifyNoMoreInteractions(localDataSource)
    }

    @Test
    fun `Sends empty result when problem in API request`() = runTest {
        val body: ResponseBody = "".toResponseBody(null)
        val response = Response.error<Void>(401, body)

        doReturn(response).`when`(remoteDataSource).getLocations(1)
        val key: Int? = null
        assertEquals(
            expected = PagingSource.LoadResult.Page(
                data = emptyList<ResultLocation>(),
                prevKey = key,
                nextKey = key
            ),
            actual = locationsPagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 10,
                    placeholdersEnabled = false
                )
            )
        )

        verify(remoteDataSource).getLocations(1)

        verifyNoMoreInteractions(remoteDataSource)
        verifyNoMoreInteractions(localDataSource)
    }
}