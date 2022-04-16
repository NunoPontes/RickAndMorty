package com.nunop.rickandmorty.utils

object AndroidTestUtils {

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }
}