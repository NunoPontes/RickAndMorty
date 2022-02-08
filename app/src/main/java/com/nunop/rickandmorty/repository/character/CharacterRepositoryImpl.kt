package com.nunop.rickandmorty.repository.character

import android.content.Context
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
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.toCharacter
import com.nunop.rickandmorty.utils.toCharacterEpisodeCrossRefList
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

    override suspend fun getCharacterByIdDb(characterId: Int): Character? {
        return localDataSource.getCharacterById(characterId)
        //TODO: get the list form the other table
    }

    override suspend fun getCharacterById(characterId: Int, context: Context?): Character? {
        val utilities = Utilities()
        if (context?.let { utilities.hasInternetConnection(it) } == true) {
            val response = remoteDataSource.getCharacterById(characterId)
            if (response.isSuccessful && response.body() != null) {
                val characterResponse = response.body()
                characterResponse?.toCharacter()?.let {
                    localDataSource.insertCharacter(
                        it
                    )
                    localDataSource.insertAllCharacterEpisodeCrossRef(characterResponse.toCharacterEpisodeCrossRefList())
                    return it
                }
                return null
            } else {
                return localDataSource.getCharacterById(characterId)
                //TODO: get the list form the other table
            }
        } else {
            return localDataSource.getCharacterById(characterId)
            //TODO: get the list form the other table
        }
    }
}