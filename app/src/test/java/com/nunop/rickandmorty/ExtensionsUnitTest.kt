package com.nunop.rickandmorty

import com.nunop.rickandmorty.data.api.models.character.Location
import com.nunop.rickandmorty.data.api.models.character.Origin
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.api.models.episode.ResultEpisode
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.utils.*
import org.junit.Test
import kotlin.test.assertEquals

class ExtensionsUnitTest {

    @Test
    fun `Next location page success returns number`() {
        assertEquals(
            expected = 2,
            actual = "https://rickandmortyapi.com/api/location?page=2".getNextLocationPage()
        )
    }

    @Test
    fun `Next location page fails returns null`() {
        assertEquals(
            expected = null,
            actual = "https://rickandmortyapi.co".getNextLocationPage()
        )
    }

    @Test
    fun `Get location id success returns number`() {
        assertEquals(
            expected = 2,
            actual = "https://rickandmortyapi.com/api/location/2".getLocationId()
        )
    }

    @Test
    fun `Get location id fails returns null`() {
        assertEquals(
            expected = null,
            actual = "https://rickandmortyapi.co".getLocationId()
        )
    }

    @Test
    fun `Get episode id success returns number`() {
        assertEquals(
            expected = 2,
            actual = "https://rickandmortyapi.com/api/episode/2".getEpisodeId()
        )
    }

    @Test
    fun `Get episode id fails returns null`() {
        assertEquals(
            expected = null,
            actual = "https://rickandmortyapi.co".getEpisodeId()
        )
    }

    @Test
    fun `Get character id success returns number`() {
        assertEquals(
            expected = 2,
            actual = "https://rickandmortyapi.com/api/character/2".getCharacterId()
        )
    }

    @Test
    fun `Get character id fails returns null`() {
        assertEquals(
            expected = null,
            actual = "https://rickandmortyapi.co".getCharacterId()
        )
    }

    @Test
    fun `Convert ResultCharacter to Character error parse location`() {
        val character = Character(
            id = 1,
            name = "a",
            status = "a",
            gender = "a",
            species = "a",
            type = "a",
            image = "a",
            originLocationId = null,
            currentLocationId = null,
            created = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            origin = Origin(
                name = "a",
                url = "a"
            ),
            url = "a",
        )
        val resultCharacter = ResultCharacter(
            created = "a",
            episode = listOf("a", "b"),
            gender = "a",
            id = 1,
            image = "a",
            location = Location(
                name = "a",
                url = "a"
            ),
            name = "a",
            origin = Origin(
                name = "a",
                url = "a"
            ),
            species = "a",
            status = "a",
            type = "a",
            url = "a"
        )
        assertEquals(
            expected = character,
            actual = resultCharacter.toCharacter()
        )
    }

    @Test
    fun `Convert ResultCharacter to Character success`() {
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
                url = "https://rickandmortyapi.com/api/location/1"
            ),
            origin = Origin(
                name = "a",
                url = "https://rickandmortyapi.com/api/location/1"
            ),
            url = "a",
        )
        val resultCharacter = ResultCharacter(
            created = "a",
            episode = listOf("a", "b"),
            gender = "a",
            id = 1,
            image = "a",
            location = Location(
                name = "a",
                url = "https://rickandmortyapi.com/api/location/1"
            ),
            name = "a",
            origin = Origin(
                name = "a",
                url = "https://rickandmortyapi.com/api/location/1"
            ),
            species = "a",
            status = "a",
            type = "a",
            url = "a"
        )
        assertEquals(
            expected = character,
            actual = resultCharacter.toCharacter()
        )
    }

    @Test
    fun `Convert ResultEpisode to Episode success`() {
        val resultEpisode = ResultEpisode(
            air_date = "a",
            characters = listOf("a", "b"),
            created = "a",
            episode = "a",
            id = 1,
            name = "a",
            url = "a"
        )
        val episode = Episode(
            id = 1,
            name = "a",
            air_date = "a",
            created = "a",
            episode = "a",
            url = "a"
        )

        assertEquals(
            expected = episode,
            actual = resultEpisode.toEpisode()
        )
    }

    @Test
    fun `Convert ResultLocation to Location success`() {
        val resultLocation = ResultLocation(
            created = "a",
            dimension = "a",
            id = 1,
            name = "a",
            residents = listOf("a", "b"),
            type = "a",
            url = "a"
        )
        val location = com.nunop.rickandmorty.data.database.entities.Location(
            id = 1,
            name = "a",
            type = "a",
            created = "a",
            dimension = "a",
            url = "a"
        )

        assertEquals(
            expected = location,
            actual = resultLocation.toLocation()
        )
    }
}