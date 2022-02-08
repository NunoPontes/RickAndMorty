package com.nunop.rickandmorty.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nunop.rickandmorty.data.api.models.character.Location
import com.nunop.rickandmorty.data.api.models.character.Origin

@Entity
data class Character(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val gender: String? = null,
    val species: String? = null,
    val type: String? = null,
    val image: String? = null,
    val originLocationId: Int? = null,
    val currentLocationId: Int? = null,
    val created: String? = null,
    @Embedded(prefix = "location")
    val location: Location? = null,
    @Embedded(prefix = "origin")
    val origin: Origin? = null,
    val url: String? = null
)
