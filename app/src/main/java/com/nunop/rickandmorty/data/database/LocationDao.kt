package com.nunop.rickandmorty.data.database

import androidx.room.*
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.database.entities.relations.LocationCharacterCrossRef
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLocationCharacterCrossRef(
        locationCharacterCrossRef:
        List<LocationCharacterCrossRef>
    )
}