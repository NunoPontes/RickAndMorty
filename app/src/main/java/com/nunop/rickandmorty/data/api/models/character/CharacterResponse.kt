package com.nunop.rickandmorty.data.api.models.character

import com.nunop.rickandmorty.data.api.models.Info

data class CharacterResponse(
    var info: Info? = null,
    var results: List<ResultCharacter>? = null
)