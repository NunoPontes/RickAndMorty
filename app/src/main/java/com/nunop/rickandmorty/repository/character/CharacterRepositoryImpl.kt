package com.nunop.rickandmorty.repository.character

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.paging.CharacterRemoteMediator
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Constants
import com.nunop.rickandmorty.utils.toCharacter
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class CharacterRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CharacterRepository {

    @ExperimentalPagingApi
    override fun getCharactersFromMediator(): Flow<PagingData<Character>> {
        val pagingSourceFactory = { localDataSource.getCharactersPaged() }

        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                maxSize = Constants.PAGE_SIZE + (Constants.PAGE_SIZE * 2),
                enablePlaceholders = false,
            ),
            remoteMediator = CharacterRemoteMediator(
                remoteDataSource,
                localDataSource
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getCharacterByIdApi(characterId: Int): Response<ResultCharacter> {
        return remoteDataSource.getCharacterById(characterId)
    }

    override suspend fun upsertCharacter(character: Character) {
        localDataSource.insertCharacter(character)
    }

    override suspend fun getCharacterByIdDb(characterId: Int): Character? {
        return localDataSource.getCharacterById(characterId)
    }

    override suspend fun getCharacterById(characterId: Int): Character? {
        val internet = true //TODO
        if (internet) {
            val response = remoteDataSource.getCharacterById(characterId)
            if (response.isSuccessful && response.body() != null) {
                val characterResponse = response.body()
                characterResponse?.toCharacter()?.let {
                    localDataSource.insertCharacter(
                        it
                    )
                    return it
                }
                return null
            } else {
                return localDataSource.getCharacterById(characterId)
            }
        } else {
            return localDataSource.getCharacterById(characterId)
        }
    }
}