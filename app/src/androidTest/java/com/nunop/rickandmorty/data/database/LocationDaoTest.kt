package com.nunop.rickandmorty.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.utils.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class LocationDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: Database

    private lateinit var dao: LocationDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.locationDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertLocationDBSuccess() = runTest {
        val location = Location(
            id = 1,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )
        dao.insertLocation(location)

        val allLocations = dao.getLocations().asLiveData().getOrAwaitValue()

        assertThat(allLocations.size).isEqualTo(1)
        assertThat(allLocations).contains(location)
    }

    @Test
    fun insertLocationDBDuplicatedSuccess() = runTest {
        val location1 = Location(
            id = 1,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )
        val location2 = Location(
            id = 1,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )
        dao.insertLocation(location1)
        dao.insertLocation(location2)

        val allLocations = dao.getLocations().asLiveData().getOrAwaitValue()

        assertThat(allLocations.size).isEqualTo(1)
        assertThat(allLocations).contains(location1)
    }

    @Test
    fun insertLocationDBMultipleSuccess() = runTest {
        val location1 = Location(
            id = 1,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )
        val location2 = Location(
            id = 2,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )
        dao.insertLocation(location1)
        dao.insertLocation(location2)

        val allLocations = dao.getLocations().asLiveData().getOrAwaitValue()

        assertThat(allLocations.size).isEqualTo(2)
        assertThat(allLocations).contains(location1)
        assertThat(allLocations).contains(location2)
    }

    @Test
    fun getLocationDBSuccess() = runTest {
        val location1 = Location(
            id = 1,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )
        val location2 = Location(
            id = 2,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )
        dao.insertLocation(location1)
        dao.insertLocation(location2)

        val allLocations = dao.getLocations().asLiveData().getOrAwaitValue()
        val locationSearch = dao.getLocationById(2)

        assertThat(locationSearch).isEqualTo(location2)
        assertThat(allLocations.size).isEqualTo(2)
        assertThat(allLocations).contains(location1)
        assertThat(allLocations).contains(location2)
    }

    @Test
    fun getLocationDBEmpty() = runTest {
        val locationSearch = dao.getLocationById(2)

        assertThat(locationSearch).isEqualTo(null)
    }
}