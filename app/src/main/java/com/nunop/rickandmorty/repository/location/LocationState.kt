package com.nunop.rickandmorty.repository.location

import com.nunop.rickandmorty.data.database.entities.Location

data class LocationState(
    val location: Location? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)