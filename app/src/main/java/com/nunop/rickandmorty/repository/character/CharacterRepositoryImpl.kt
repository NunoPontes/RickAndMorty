package com.nunop.rickandmorty.repository.character

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.paging.CharacterRemoteMediator
import com.nunop.rickandmorty.data.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.data.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.utils.Constants
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.toCharacter
import com.nunop.rickandmorty.utils.toCharacterEpisodeCrossRefList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ExperimentalPagingApi
class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val context: Context?,
    private val characterRemoteMediator: CharacterRemoteMediator
) : CharacterRepository {

    @ExperimentalPagingApi
    override fun getCharactersFromMediator(): Flow<PagingData<Character>> {
        val pagingSourceFactory = { localDataSource.getCharactersPaged() }

        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = characterRemoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getCharacterByIdApi(characterId: Int): Response<ResultCharacter> {
        return remoteDataSource.getCharacterById(characterId)
    }

    override suspend fun getCharacterByIdDb(characterId: Int): Character? {
        return localDataSource.getCharacterById(characterId)
    }

    override suspend fun getCharacterById(characterId: Int): Character? {
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
            }
        } else {
            return localDataSource.getCharacterById(characterId)
        }
    }
}