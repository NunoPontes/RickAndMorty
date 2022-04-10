package com.nunop.rickandmorty

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nunop.rickandmorty.data.api.models.character.Location
import com.nunop.rickandmorty.data.api.models.character.Origin
import com.nunop.rickandmorty.data.database.CharacterDao
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.Character
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
class CharacterDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: Database

    private lateinit var dao: CharacterDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.characterDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCharacterDBSuccess() = runTest {
        val character = Character(
            id = 1,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        dao.insertCharacter(character)

        val allCharacters = dao.getCharacters().asLiveData().getOrAwaitValue()

        assertThat(allCharacters.size).isEqualTo(1)
        assertThat(allCharacters).contains(character)
    }

    @Test
    fun insertCharacterDBDuplicatedSuccess() = runTest {
        val character1 = Character(
            id = 1,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        val character2 = Character(
            id = 1,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        dao.insertAll(listOf(character1, character2))

        val allCharacters = dao.getCharacters().asLiveData().getOrAwaitValue()

        assertThat(allCharacters.size).isEqualTo(1)
        assertThat(allCharacters).contains(character1)
    }

    @Test
    fun insertCharacterDBMultipleSuccess() = runTest {
        val character1 = Character(
            id = 1,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        val character2 = Character(
            id = 2,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        dao.insertAll(listOf(character1, character2))

        val allCharacters = dao.getCharacters().asLiveData().getOrAwaitValue()

        assertThat(allCharacters.size).isEqualTo(2)
        assertThat(allCharacters).contains(character1)
        assertThat(allCharacters).contains(character2)
    }

    @Test
    fun getCharacterDBSuccess() = runTest {
        val character1 = Character(
            id = 1,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        val character2 = Character(
            id = 2,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        dao.insertAll(listOf(character1, character2))

        val allCharacters = dao.getCharacters().asLiveData().getOrAwaitValue()

        val characterSearch = dao.getCharacterById(2)

        assertThat(characterSearch).isEqualTo(character2)
        assertThat(allCharacters.size).isEqualTo(2)
        assertThat(allCharacters).contains(character1)
        assertThat(allCharacters).contains(character2)
    }

    @Test
    fun getCharacterDBEmpty() = runTest {
        val characterSearch = dao.getCharacterById(2)

        assertThat(characterSearch).isEqualTo(null)
    }

    @Test
    fun deleteAllCharactersDBSuccess() = runTest {
        val character1 = Character(
            id = 1,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        val character2 = Character(
            id = 2,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = 1,
            currentLocationId = 1,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a"
        )
        dao.insertAll(listOf(character1, character2))

        var allCharacters = dao.getCharacters().asLiveData().getOrAwaitValue()

        assertThat(allCharacters.size).isEqualTo(2)
        assertThat(allCharacters).contains(character1)
        assertThat(allCharacters).contains(character2)

        dao.deleteAll()

        allCharacters = dao.getCharacters().asLiveData().getOrAwaitValue()
        assertThat(allCharacters.size).isEqualTo(0)
    }
}