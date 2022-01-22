package com.nunop.rickandmorty.data.api.models.location

data class ResultLocation(
    var created: String? = null,
    var dimension: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var residents: List<String>? = null,
    var type: String? = null,
    var url: String? = null
)