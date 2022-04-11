package com.nunop.rickandmorty.data.datasource.localdatasource

import com.nunop.rickandmorty.data.database.entities.CharacterRemoteKey

interface CharacterRemoteKeyLocalDataSource {

    suspend fun insertAllCharacterRemoteKey(remoteKey: List<CharacterRemoteKey>)

    suspend fun remoteKeysCharacterId(id: Int): CharacterRemoteKey?

    suspend fun deleteAllCharacterRemoteKey()
}