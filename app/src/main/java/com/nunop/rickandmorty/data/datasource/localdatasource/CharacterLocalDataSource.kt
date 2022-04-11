package com.nunop.rickandmorty.data.datasource.localdatasource

import androidx.paging.PagingSource
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {

    suspend fun insertCharacter(character: Character)

    suspend fun insertAllCharacters(characters: List<Character>)

    suspend fun getCharacterById(id: Int): Character?

    fun getCharacters(): Flow<List<Character>>

    fun getCharactersPaged(): PagingSource<Int, Character>

    suspend fun deleteAllCharacters()

    suspend fun insertCharacterEpisodeCrossRef(characterEpisodeCrossRef: CharacterEpisodeCrossRef)

    suspend fun insertAllCharacterEpisodeCrossRef(
        characterEpisodeCrossRef:
        List<CharacterEpisodeCrossRef>
    )
}