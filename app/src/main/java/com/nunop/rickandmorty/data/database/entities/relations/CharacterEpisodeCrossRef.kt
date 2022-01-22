package com.nunop.rickandmorty.data.database.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["characterId", "episodeId"])
data class CharacterEpisodeCrossRef(
    val characterId: Int,
    val episodeId: Int
)
