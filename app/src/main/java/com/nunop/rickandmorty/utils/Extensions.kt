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
        currentLocationId = this.location?.url?.getLocationId()
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
    air_date = air_date
)

fun ResultLocation.toLocation() = Location(
    id = id,
    name = name,
    type = type
)

fun String.getLocationId(): Int? {
    return try {
        this.substringAfterLast("https://rickandmortyapi.com/api/location/").toInt()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
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