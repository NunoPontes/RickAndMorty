package com.nunop.rickandmorty.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val originLocationId: Int? = null,
    val currentLocationId: Int? = null
)
