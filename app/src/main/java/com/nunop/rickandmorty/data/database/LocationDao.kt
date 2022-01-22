package com.nunop.rickandmorty.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.LocationWithCharacters
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM location WHERE id = :id")
    suspend fun getLocationById(id: Int): Location?

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM location")
    fun getLocations(): Flow<List<Location>>


    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM location WHERE id = :locationId")
    suspend fun getLocationWithCharacters(locationId: Int): List<LocationWithCharacters>
}