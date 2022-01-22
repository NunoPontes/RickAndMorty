package com.nunop.rickandmorty.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.Dao
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.LocationWithCharacters
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: Episode)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<Episode>)

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM episode WHERE id = :id")
    suspend fun getEpisodeById(id: Int): Episode?

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM episode")
    fun getEpisodes(): LiveData<List<Episode>>

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM episode")
    fun getEpisodesPaged(): PagingSource<Int, Episode>

    @Query("DELETE FROM episode")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterEpisodeCrossRef(characterEpisodeCrossRef: CharacterEpisodeCrossRef)

}