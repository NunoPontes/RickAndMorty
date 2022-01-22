package com.nunop.rickandmorty.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Location

data class LocationWithCharacters(
    @Embedded val location: Location,
    @Relation(
        parentColumn = "id",
        entityColumn = "currentLocationId"
    )
    val characters: List<Character>
)
