package com.nunop.rickandmorty.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Episode

data class EpisodeWithCharacters(
    @Embedded val episode: Episode,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(CharacterEpisodeCrossRef::class)
    )
    val characters: List<Character>
)
