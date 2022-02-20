package com.nunop.rickandmorty.datasource.localdatasource

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.*
import com.nunop.rickandmorty.data.database.entities.relations.CharacterEpisodeCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.EpisodeCharacterCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.LocationCharacterCrossRef
import com.nunop.rickandmorty.data.database.entities.relations.LocationWithCharacters
import com.nunop.rickandmorty.utils.Resource
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val db: Database) :
    CharacterLocalDataSource,
    CharacterRemoteKeyLocalDataSource,
    EpisodeLocalDataSource,
    EpisodeRemoteKeyLocalDataSource,
    LocationLocalDataSource {

    fun getDatabase(): Database = db

    //--------------------------Character--------------------------
    override suspend fun insertCharacter(character: Character) {
        db.characterDao.insertCharacter(character)
    }

    override suspend fun insertAllCharacters(characters: List<Character>) {
        db.characterDao.insertAll(characters)
    }

    override suspend fun getCharacterById(id: Int): Character? {
        return db.characterDao.getCharacterById(id)
    }

    override fun getCharacters(): Flow<List<Character>> {
        return db.characterDao.getCharacters()
    }

    override fun getCharactersPaged(): PagingSource<Int, Character> {
        return db.characterDao.getCharactersPaged()
    }

    override suspend fun deleteAllCharacters() {
        db.characterDao.deleteAll()
    }

    override suspend fun insertCharacterEpisodeCrossRef(characterEpisodeCrossRef: CharacterEpisodeCrossRef) {
        db.characterDao.insertCharacterEpisodeCrossRef(characterEpisodeCrossRef)
    }

    override suspend fun insertAllCharacterEpisodeCrossRef(characterEpisodeCrossRef: List<CharacterEpisodeCrossRef>) {
        db.characterDao.insertAllCharacterEpisodeCrossRef(characterEpisodeCrossRef)
    }


    //--------------------------Character Remote Key--------------------------
    override suspend fun insertAllCharacterRemoteKey(remoteKey: List<CharacterRemoteKey>) {
        db.characterRemoteKeyDao.insertAll(remoteKey)
    }

    override suspend fun remoteKeysCharacterId(id: Int): CharacterRemoteKey? {
        return db.characterRemoteKeyDao.remoteKeysCharacterId(id)
    }

    override suspend fun deleteAllCharacterRemoteKey() {
        db.characterRemoteKeyDao.deleteAll()
    }


    //--------------------------Episode--------------------------
    override suspend fun insertEpisode(episode: Episode) {
        db.episodeDao.insertEpisode(episode)
    }

    override suspend fun insertAllEpisodes(episodes: List<Episode>) {
        db.episodeDao.insertAll(episodes)
    }

    override suspend fun getEpisodeById(id: Int): Episode? {
        return db.episodeDao.getEpisodeById(id)
    }

    override fun getEpisodes(): LiveData<List<Episode>> {
        return db.episodeDao.getEpisodes()
    }

    override fun getEpisodesPaged(): PagingSource<Int, Episode> {
        return db.episodeDao.getEpisodesPaged()
    }

    override suspend fun deleteAllEpisodes() {
        db.episodeDao.deleteAll()
    }

    override suspend fun insertAllEpisodeCharacterCrossRef(episodeCharacterCrossRef: List<EpisodeCharacterCrossRef>) {
        return db.episodeDao.insertAllEpisodeCharacterCrossRef(episodeCharacterCrossRef)
    }


    //--------------------------Episode Remote Key--------------------------
    override suspend fun insertAllEpisodeRemoteKey(remoteKey: List<EpisodeRemoteKey>) {
        db.episodeRemoteKeyDao.insertAll(remoteKey)
    }


    override suspend fun remoteKeysEpisodeId(id: Int): EpisodeRemoteKey? {
        return db.episodeRemoteKeyDao.remoteKeysEpisodeId(id)
    }

    override suspend fun deleteAllEpisodeRemoteKey() {
        db.episodeRemoteKeyDao.deleteAll()
    }


    //--------------------------Location--------------------------
    override suspend fun insertLocation(location: Location) {
        db.locationDao.insertLocation(location)
    }

    override suspend fun getLocationById(id: Int): Location? {
        return db.locationDao.getLocationById(id)
    }

    override fun getLocations(): Flow<List<Location>> {
        return db.locationDao.getLocations()
    }

    override suspend fun getLocationWithCharacters(locationId: Int): List<LocationWithCharacters> {
        return db.locationDao.getLocationWithCharacters(locationId)
    }

    override suspend fun insertAllLocationCharacterCrossRef(locationCharacterCrossRef: List<LocationCharacterCrossRef>) {
        return db.locationDao.insertAllLocationCharacterCrossRef(locationCharacterCrossRef)
    }


}