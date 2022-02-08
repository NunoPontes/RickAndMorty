package com.nunop.rickandmorty.data.database.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["id", "episodeId"])
data class LocationCharacterCrossRef(
    val id: Int,
    val episodeId: Int
)
