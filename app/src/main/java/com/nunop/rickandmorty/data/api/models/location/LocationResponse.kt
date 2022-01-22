package com.nunop.rickandmorty.data.api.models.location

data class LocationResponse(
    var info: Info? = null,
    var results: List<ResultLocation>? = null
)