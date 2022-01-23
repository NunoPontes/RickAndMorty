package com.nunop.rickandmorty.repository.character

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nunop.rickandmorty.api.RickAndMortyAPI
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.data.paging.CharacterRemoteMediator
import com.nunop.rickandmorty.utils.Constants
import kotlinx.coroutines.flow.Flow

class CharacterRepositoryImpl(
    private val api: RickAndMortyAPI,
    private val db: Database
) : CharacterRepository {


    @ExperimentalPagingApi
    override fun getCharactersFromMediator(): Flow<PagingData<Character>> {
        val pagingSourceFactory = { db.characterDao.getCharactersPaged() }

        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                maxSize = Constants.PAGE_SIZE + (Constants.PAGE_SIZE * 2),
                enablePlaceholders = false,
            ),
            remoteMediator = CharacterRemoteMediator(
                api,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}