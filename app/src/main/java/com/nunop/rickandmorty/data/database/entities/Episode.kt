package com.nunop.rickandmorty.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Episode(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val created: String? = null,
    val episode: String? = null,
    val url: String? = null
)
