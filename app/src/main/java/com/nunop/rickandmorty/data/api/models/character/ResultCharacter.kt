package com.nunop.rickandmorty.data.api.models.character

data class ResultCharacter(
    var created: String? = null,
    var episode: List<String>? = null,
    var gender: String? = null,
    var id: Int? = null,
    var image: String? = null,
    var location: Location? = null,
    var name: String? = null,
    var origin: Origin? = null,
    var species: String? = null,
    var status: String? = null,
    var type: String? = null,
    var url: String? = null
)