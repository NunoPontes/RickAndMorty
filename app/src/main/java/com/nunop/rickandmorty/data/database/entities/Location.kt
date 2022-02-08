package com.nunop.rickandmorty.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val created: String? = null,
    val dimension: String? = null,
    val url: String? = null
)
