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
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getCharacterById(id: Int): Character?

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM character")
    fun getCharacters(): Flow<List<Character>>


    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM location WHERE id = :locationId")
    suspend fun getLocationWithCharacters(locationId: Int): List<LocationWithCharacters>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterEpisodeCrossRef(characterEpisodeCrossRef: CharacterEpisodeCrossRef)

}