package com.nunop.rickandmorty.data.api.models.episode

data class EpisodeResponse(
    var info: Info? = null,
    var results: List<ResultEpisode>? = null
)