package com.nunop.rickandmorty.data.database

import androidx.room.*
import com.nunop.rickandmorty.data.database.entities.CharacterRemoteKey

@Dao
interface CharacterRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CharacterRemoteKey>)

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM characterremotekey WHERE id = :id")
    suspend fun remoteKeysCharacterId(id: Int): CharacterRemoteKey?

    @Query("DELETE FROM characterremotekey")
    suspend fun deleteAll()
}

