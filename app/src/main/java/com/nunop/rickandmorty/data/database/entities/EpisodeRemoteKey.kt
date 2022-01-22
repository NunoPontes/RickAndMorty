package com.nunop.rickandmorty.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EpisodeRemoteKey(
    @PrimaryKey(autoGenerate = false) val id: Int? = null,
    val prevKey: Int?,
    val nextKey: Int?
)
