package com.nunop.rickandmorty.data.api.models.episode

import com.nunop.rickandmorty.data.api.models.Info

data class EpisodeResponse(
    var info: Info? = null,
    var results: List<ResultEpisode>? = null
)