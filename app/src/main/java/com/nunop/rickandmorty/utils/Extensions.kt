package com.nunop.rickandmorty.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.api.models.episode.ResultEpisode
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.EpisodeCharacterCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.LocationCharacterCrossRef
import timber.log.Timber

fun ResultCharacter.toCharacter(): Character {
    return Character(
        id = this.id,
        name = this.name,
        status = this.status,
        gender = this.gender,
        species = this.species,
        type = this.type,
        image = this.image,
        originLocationId = this.origin?.url?.getLocationId(),
        currentLocationId = this.location?.url?.getLocationId(),
        created = this.created,
        location = this.location,
        origin = this.origin,
        url = this.url
    )
}

fun List<ResultCharacter>.toListCharacters(): List<Character> {
    val listCharacter: MutableList<Character> = mutableListOf()
    forEach {
        listCharacter.add(it.toCharacter())
    }
    return listCharacter
}

fun List<ResultEpisode>.toListEpisodes(): List<Episode> {
    val listEpisode: MutableList<Episode> = mutableListOf()
    forEach {
        listEpisode.add(it.toEpisode())
    }
    return listEpisode
}

fun ResultEpisode.toEpisode() = Episode(
    id = id,
    name = name,
    air_date = air_date,
    created = created,
    episode = episode,
    url = url
)

fun ResultLocation.toLocation() = Location(
    id = id,
    name = name,
    type = type,
    created = created,
    dimension = dimension,
    url = url
)

fun String.getLocationId(): Int? {
    return try {
        this.substringAfterLast("https://rickandmortyapi.com/api/location/").toInt()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun String.getEpisodeId(): Int? {
    return try {
        this.substringAfterLast("https://rickandmortyapi.com/api/episode/").toInt()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun String.getCharacterId(): Int? {
    return try {
        this.substringAfterLast("https://rickandmortyapi.com/api/character/").toInt()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun ResultCharacter.toCharacterEpisodeCrossRefList(): List<CharacterEpisodeCrossRef> {
    val list = mutableListOf<CharacterEpisodeCrossRef>()

    this.episode?.forEach { episode ->
        this.id?.let { episode.getEpisodeId()?.let { it1 -> CharacterEpisodeCrossRef(it, it1) } }
            ?.let { list.add(it) }
    }
    return list
}

fun ResultEpisode.toEpisodeCharacterCrossRefList(): List<EpisodeCharacterCrossRef> {
    val list = mutableListOf<EpisodeCharacterCrossRef>()

    this.characters?.forEach { character ->
        this.id?.let {
            character.getCharacterId()?.let { it1 -> EpisodeCharacterCrossRef(it1, it) }
        }
            ?.let { list.add(it) }
    }
    return list
}

fun ResultLocation.toLocationCharacterCrossRefList(): List<LocationCharacterCrossRef> {
    val list = mutableListOf<LocationCharacterCrossRef>()

    this.residents?.forEach { residents ->
        this.id?.let {
            residents.getCharacterId()?.let { it1 -> LocationCharacterCrossRef(it, it1) }
        }
            ?.let { list.add(it) }
    }
    return list
}

/**
 * Protection against illegal state exceptions
 */
fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    try {
        navigate(resId, args, navOptions, navExtras)
    } catch (e: Exception) {
        Timber.e(e.message ?: "Exception")
    }
}