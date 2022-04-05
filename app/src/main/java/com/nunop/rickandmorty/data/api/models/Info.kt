package com.nunop.rickandmorty.data.api.models

data class Info(
    var count: Int? = null,
    var next: String? = null,
    var pages: Int? = null,
    var prev: Any? = null
)