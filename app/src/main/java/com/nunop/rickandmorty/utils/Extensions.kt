package com.nunop.rickandmorty.utils

import android.util.Log
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.api.models.episode.ResultEpisode
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.database.entities.Location

fun ResultCharacter.toCharacter():com.nunop.rickandmorty.data.database.entities.Character  {
    return com.nunop.rickandmorty.data.database.entities.Character(
        id = this.id,
        name = this.name,
        status = this.status,
        originLocationId = this.origin?.url?.getLocationId(),
        currentLocationId = this.location?.url?.getLocationId()
    )
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

fun String.getLocationId(): Int?{
    return try {
        this.substringAfterLast("https://rickandmortyapi.com/api/location/").toInt()
    } catch (e: Exception){
        Log.e("AAAAAAAAA",this)
        null
    }
}