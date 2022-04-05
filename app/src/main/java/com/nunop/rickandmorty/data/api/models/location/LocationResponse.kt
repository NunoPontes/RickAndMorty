package com.nunop.rickandmorty.data.api.models.location

import com.nunop.rickandmorty.data.api.models.Info

data class LocationResponse(
    var info: Info? = null,
    var results: List<ResultLocation>? = null
)