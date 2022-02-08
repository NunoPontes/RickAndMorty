package com.nunop.rickandmorty.data.database

import androidx.paging.PagingSource
import androidx.room.*
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getCharacterById(id: Int): Character?

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM character")
    fun getCharacters(): Flow<List<Character>>

    @Transaction //To ensure this happens atomically
    @Query("SELECT * FROM character")
    fun getCharactersPaged(): PagingSource<Int, Character>

    @Query("DELETE FROM character")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterEpisodeCrossRef(characterEpisodeCrossRef: CharacterEpisodeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCharacterEpisodeCrossRef(
        characterEpisodeCrossRef:
        List<CharacterEpisodeCrossRef>
    )

}