package com.nunop.rickandmorty.data.database

import androidx.room.*
import com.nunop.rickandmorty.data.database.entities.EpisodeRemoteKey

@Dao
interface EpisodeRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<EpisodeRemoteKey>)

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM episoderemotekey WHERE id = :id")
    suspend fun remoteKeysEpisodeId(id: Int): EpisodeRemoteKey?

    @Query("DELETE FROM episoderemotekey")
    suspend fun deleteAll()
}

