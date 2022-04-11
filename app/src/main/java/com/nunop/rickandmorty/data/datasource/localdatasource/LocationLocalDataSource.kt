package com.nunop.rickandmorty.data.datasource.localdatasource

import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.data.database.entities.relations.LocationCharacterCrossRef
import kotlinx.coroutines.flow.Flow

interface LocationLocalDataSource {

    suspend fun insertLocation(location: Location)

    suspend fun getLocationById(id: Int): Location?

    fun getLocations(): Flow<List<Location>>

    suspend fun insertAllLocationCharacterCrossRef(
        locationCharacterCrossRef:
        List<LocationCharacterCrossRef>
    )
}