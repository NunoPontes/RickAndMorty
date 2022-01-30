package com.nunop.rickandmorty.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.nunop.rickandmorty.data.database.entities.Episode

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
}