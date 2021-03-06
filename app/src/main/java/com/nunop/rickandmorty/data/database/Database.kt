package com.nunop.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nunop.rickandmorty.data.database.entities.*
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.EpisodeCharacterCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.LocationCharacterCrossRef

@Database(
    entities = [
        Character::class,
        Episode::class,
        Location::class,
        CharacterEpisodeCrossRef::class,
        EpisodeRemoteKey::class,
        CharacterRemoteKey::class,
        EpisodeCharacterCrossRef::class,
        LocationCharacterCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract val characterDao: CharacterDao
    abstract val episodeDao: EpisodeDao
    abstract val episodeRemoteKeyDao: EpisodeRemoteKeyDao
    abstract val characterRemoteKeyDao: CharacterRemoteKeyDao
    abstract val locationDao: LocationDao
}