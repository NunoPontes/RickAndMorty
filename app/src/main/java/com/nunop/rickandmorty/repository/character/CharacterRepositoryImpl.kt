package com.nunop.rickandmorty.repository.character

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.database.CharacterDao
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.paging.CharactersPagingDataSource
import com.nunop.rickandmorty.repository.location.LocationRepository
import kotlinx.coroutines.flow.Flow

class CharacterRepositoryImpl(
    private val characterDao: CharacterDao,
    private val dataSource: CharactersPagingDataSource
): CharacterRepository {

    override suspend fun getCharacters(pageNumber: Int) = RetrofitInstance.api.getCharacters(pageNumber)

    override fun getAllCharactersDao() = characterDao.getCharacters()

    override suspend fun insertCharacter(character: Character) = characterDao.insertCharacter(character)

    override suspend fun getAllCharacters(): Flow<PagingData<ResultCharacter>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { dataSource }
    ).flow

//    suspend fun getCharacters() {
//        val characters = getCharacters(1)
//        characters.
//    }
}