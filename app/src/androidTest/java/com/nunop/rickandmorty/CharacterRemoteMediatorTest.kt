package com.nunop.rickandmorty

import androidx.paging.*
import androidx.test.filters.SmallTest
import com.nunop.rickandmorty.data.api.models.Info
import com.nunop.rickandmorty.data.api.models.character.CharacterResponse
import com.nunop.rickandmorty.data.api.models.character.Location
import com.nunop.rickandmorty.data.api.models.character.Origin
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.paging.CharacterRemoteMediator
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@SmallTest
@HiltAndroidTest
class CharacterRemoteMediatorTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var characterRemoteMediator: CharacterRemoteMediator

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    @MockK
    private lateinit var localDataSource: LocalDataSource

    @Inject
    @Named("test_db")
    lateinit var mockDb: Database

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        characterRemoteMediator = CharacterRemoteMediator(remoteDataSource, localDataSource)
        hiltRule.inject()

        every { localDataSource.getDatabase() } returns mockDb
    }

    @After
    fun teardown() {
        mockDb.clearAllTables()
    }

    @Test
    fun successResultWhenDataIsPresent() = runTest {
        val resultCharacter = ResultCharacter(
            created = "",
            episode = listOf(""),
            gender = "",
            id = 1,
            image = "",
            location = Location(
                name = "",
                url = ""
            ),
            name = "",
            origin = Origin(
                name = "",
                url = ""
            ),
            species = "",
            status = "",
            type = "",
            url = ""
        )
        val results = listOf(resultCharacter)
        val characterResponse = CharacterResponse(
            info = Info(
                count = null,
                next = "https://rickandmortyapi.com/api/character?page=2",
                pages = null,
                prev = null
            ),
            results = results
        )
        val listResponse: Response<CharacterResponse> =
            Response.success(characterResponse)

        coEvery { remoteDataSource.getCharacters(1) } returns listResponse

        val pagingState = PagingState<Int, Character>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = characterRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { remoteDataSource.getCharacters(1) }
        coVerify { localDataSource.getDatabase() }
        coVerify { localDataSource.deleteAllCharacters() }
        coVerify { localDataSource.deleteAllCharacterRemoteKey() }
        coVerify { localDataSource.insertAllCharacterRemoteKey(any()) }
        coVerify { localDataSource.insertAllCharacters(any()) }

        confirmVerified(remoteDataSource)
        confirmVerified(localDataSource)
    }

    @Test
    fun successResultWhenNoMoreDataIsPresent() = runTest {
        val characterResponse = CharacterResponse(
            info = Info(
                count = null,
                next = "https://rickandmortyapi.com/api/character?page=2",
                pages = null,
                prev = null
            ),
            results = listOf()
        )
        val listResponse: Response<CharacterResponse> =
            Response.success(characterResponse)

        coEvery { remoteDataSource.getCharacters(1) } returns listResponse

        val pagingState = PagingState<Int, Character>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = characterRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        coVerify { remoteDataSource.getCharacters(1) }
        coVerify { localDataSource.getDatabase() }
        coVerify { localDataSource.deleteAllCharacters() }
        coVerify { localDataSource.deleteAllCharacterRemoteKey() }
        coVerify { localDataSource.insertAllCharacterRemoteKey(emptyList()) }
        coVerify { localDataSource.insertAllCharacters(emptyList()) }

        confirmVerified(remoteDataSource)
        confirmVerified(localDataSource)
    }

    @Test
    fun errorResultWhenErrorOccurs() = runTest {
        coEvery { remoteDataSource.getCharacters(1) } throws Exception("")

        val pagingState = PagingState<Int, Character>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = characterRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)

        coVerify { remoteDataSource.getCharacters(1) }

        confirmVerified(remoteDataSource)
        confirmVerified(localDataSource)
    }
}