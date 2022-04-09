package com.nunop.rickandmorty

import androidx.paging.*
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.nunop.rickandmorty.data.api.models.Info
import com.nunop.rickandmorty.data.api.models.episode.EpisodeResponse
import com.nunop.rickandmorty.data.api.models.episode.ResultEpisode
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.paging.EpisodeRemoteMediator
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response


@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class EpisodeRemoteMediatorTest {

    private lateinit var episodeRemoteMediator: EpisodeRemoteMediator

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource
    private lateinit var mockDb: Database

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        episodeRemoteMediator = EpisodeRemoteMediator(remoteDataSource, localDataSource)

        mockDb = Database.getInstance(ApplicationProvider.getApplicationContext())

        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )

        val transactionLambda = slot<suspend () -> R>()
        coEvery { mockDb.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        every { localDataSource.getDatabase() } returns mockDb
    }

    @After
    fun teardown() {
        mockDb.clearAllTables()
    }

    @Test
    fun successResultWhenDataIsPresent() = runTest {
        val resultEpisode = ResultEpisode(
            air_date = "",
            characters = listOf(),
            created = "",
            episode = "",
            id = 1,
            name = "",
            url = ""
        )
        val results = listOf(resultEpisode)
        val episodeResponse = EpisodeResponse(
            info = Info(
                count = null,
                next = "https://rickandmortyapi.com/api/episode?page=2",
                pages = null,
                prev = null
            ),
            results = results
        )
        val listResponse: Response<EpisodeResponse> =
            Response.success(episodeResponse)

        coEvery { remoteDataSource.getEpisodes(1) } returns listResponse

        val pagingState = PagingState<Int, Episode>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = episodeRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { remoteDataSource.getEpisodes(1) }
        coVerify { localDataSource.getDatabase() }
        coVerify { localDataSource.deleteAllEpisodes() }
        coVerify { localDataSource.deleteAllEpisodeRemoteKey() }
        coVerify { localDataSource.insertAllEpisodeRemoteKey(any()) }
        coVerify { localDataSource.insertAllEpisodes(any()) }

        confirmVerified(remoteDataSource)
        confirmVerified(localDataSource)
    }

    @Test
    fun successResultWhenNoMoreDataIsPresent() = runTest {
        val episodeResponse = EpisodeResponse(
            info = Info(
                count = null,
                next = "https://rickandmortyapi.com/api/episode?page=2",
                pages = null,
                prev = null
            ),
            results = listOf()
        )
        val listResponse: Response<EpisodeResponse> =
            Response.success(episodeResponse)

        coEvery { remoteDataSource.getEpisodes(1) } returns listResponse

        val pagingState = PagingState<Int, Episode>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = episodeRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { remoteDataSource.getEpisodes(1) }
        coVerify { localDataSource.getDatabase() }
        coVerify { localDataSource.deleteAllEpisodes() }
        coVerify { localDataSource.deleteAllEpisodeRemoteKey() }
        coVerify { localDataSource.insertAllEpisodeRemoteKey(emptyList()) }
        coVerify { localDataSource.insertAllEpisodes(emptyList()) }

        confirmVerified(remoteDataSource)
        confirmVerified(localDataSource)
    }

    @Test
    fun errorResultWhenErrorOccurs() = runTest {
        coEvery { remoteDataSource.getEpisodes(1) } throws Exception("")

        val pagingState = PagingState<Int, Episode>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = episodeRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)

        coVerify { remoteDataSource.getEpisodes(1) }

        confirmVerified(remoteDataSource)
        confirmVerified(localDataSource)
    }
}