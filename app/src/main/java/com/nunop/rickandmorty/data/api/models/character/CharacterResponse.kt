package com.nunop.rickandmorty.data.api.models.character

data class CharacterResponse(
    var info: Info? = null,
    var results: List<ResultCharacter>? = null
)