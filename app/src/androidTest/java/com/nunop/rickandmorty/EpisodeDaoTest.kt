package com.nunop.rickandmorty

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.EpisodeDao
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class EpisodeDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: Database
    private lateinit var dao: EpisodeDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            Database::class.java
        ).allowMainThreadQueries().build()
        dao = database.episodeDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertEpisodeDBSuccess() = runTest {
        val episode = Episode(
            id = 1,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        dao.insertEpisode(episode)

        val allEpisodes = dao.getEpisodes().getOrAwaitValue()

        assertThat(allEpisodes.size).isEqualTo(1)
        assertThat(allEpisodes).contains(episode)
    }

    @Test
    fun insertEpisodeDBDuplicatedSuccess() = runTest {
        val episode1 = Episode(
            id = 1,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        val episode2 = Episode(
            id = 1,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        dao.insertAll(listOf(episode1, episode2))

        val allEpisodes = dao.getEpisodes().getOrAwaitValue()

        assertThat(allEpisodes.size).isEqualTo(1)
        assertThat(allEpisodes).contains(episode1)
    }

    @Test
    fun insertEpisodeDBMultipleSuccess() = runTest {
        val episode1 = Episode(
            id = 1,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        val episode2 = Episode(
            id = 2,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        dao.insertAll(listOf(episode1, episode2))

        val allEpisodes = dao.getEpisodes().getOrAwaitValue()

        assertThat(allEpisodes.size).isEqualTo(2)
        assertThat(allEpisodes).contains(episode1)
        assertThat(allEpisodes).contains(episode2)
    }

    @Test
    fun getEpisodeDBSuccess() = runTest {
        val episode1 = Episode(
            id = 1,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        val episode2 = Episode(
            id = 2,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        dao.insertAll(listOf(episode1, episode2))

        val allEpisodes = dao.getEpisodes().getOrAwaitValue()

        val episodeSearch = dao.getEpisodeById(2)

        assertThat(episodeSearch).isEqualTo(episode2)
        assertThat(allEpisodes.size).isEqualTo(2)
        assertThat(allEpisodes).contains(episode1)
        assertThat(allEpisodes).contains(episode2)
    }

    @Test
    fun getEpisodeDBEmpty() = runTest {
        val episodeSearch = dao.getEpisodeById(2)

        assertThat(episodeSearch).isEqualTo(null)
    }

    @Test
    fun deleteAllEpisodesDBSuccess() = runTest {
        val episode1 = Episode(
            id = 1,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        val episode2 = Episode(
            id = 2,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )
        dao.insertAll(listOf(episode1, episode2))

        var allEpisodes = dao.getEpisodes().getOrAwaitValue()

        assertThat(allEpisodes.size).isEqualTo(2)
        assertThat(allEpisodes).contains(episode1)
        assertThat(allEpisodes).contains(episode2)

        dao.deleteAll()

        allEpisodes = dao.getEpisodes().getOrAwaitValue()
        assertThat(allEpisodes.size).isEqualTo(0)
    }
}