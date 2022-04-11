package com.nunop.rickandmorty.data.datasource.localdatasource

import com.nunop.rickandmorty.data.database.entities.EpisodeRemoteKey

interface EpisodeRemoteKeyLocalDataSource {

    suspend fun insertAllEpisodeRemoteKey(remoteKey: List<EpisodeRemoteKey>)

    suspend fun remoteKeysEpisodeId(id: Int): EpisodeRemoteKey?

    suspend fun deleteAllEpisodeRemoteKey()
}