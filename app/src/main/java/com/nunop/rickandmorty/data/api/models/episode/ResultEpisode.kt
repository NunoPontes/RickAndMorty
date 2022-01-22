package com.nunop.rickandmorty.data.api.models.episode

data class ResultEpisode(
    var air_date: String? = null,
    var characters: List<String>? = null,
    var created: String? = null,
    var episode: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var url: String? = null
)