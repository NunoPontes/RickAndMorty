package com.nunop.rickandmorty.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.database.entities.EpisodeRemoteKey
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef

@Database(
    entities = [
        Character::class,
        Episode::class,
        Location::class,
        CharacterEpisodeCrossRef::class,
        EpisodeRemoteKey::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract val characterDao: CharacterDao
    abstract val episodeDao: EpisodeDao
    abstract val episodeRemoteKeyDao: RemoteKeyDao
    abstract val locationDao: LocationDao

    companion object {
        @Volatile
        private var INSTANCE: com.nunop.rickandmorty.data.database.Database? = null

        fun getInstance(context: Context): com.nunop.rickandmorty.data.database.Database {
            synchronized(this) {
                return INSTANCE ?: createDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            com.nunop.rickandmorty.data.database.Database::class.java,
            "ricky_db"
        ).build()
    }
}