package com.nunop.rickandmorty.data.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.data.database.CharacterDao
import com.nunop.rickandmorty.utils.toCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersPagingDataSource(private val characterDao:CharacterDao) :
    PagingSource<Int, com.nunop.rickandmorty.data.api.models.character.ResultCharacter>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.nunop.rickandmorty.data.api.models.character.ResultCharacter> {
        val pageNumber = params.key ?: 1
        return try {
            val response = RetrofitInstance.api.getCharacters(pageNumber)
            val pagedResponse = response.body()
            val data = pagedResponse?.results

            var nextPageNumber: Int? = null
            pagedResponse?.info?.next?.let {
                val uri = Uri.parse(it)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            //Insert on the DB
            withContext(Dispatchers.IO) {
                data?.forEach {
                    characterDao.insertCharacter(it.toCharacter())
                }
            }

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, com.nunop.rickandmorty.data.api.models.character.ResultCharacter>): Int =
        1
}