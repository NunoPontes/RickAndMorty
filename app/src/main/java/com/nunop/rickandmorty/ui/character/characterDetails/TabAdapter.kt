package com.nunop.rickandmorty.ui.character.characterDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nunop.rickandmorty.ui.character.characterDetails.characterInfo.CharacterInfoFragment
import com.nunop.rickandmorty.ui.episode.episodes.EpisodesFragment
import com.nunop.rickandmorty.ui.location.locations.LocationsFragment

@ExperimentalPagingApi
class TabAdapter(fa: FragmentActivity, private val characterId: Int) :
    FragmentStateAdapter(fa) {

    //TODO: find better way to avoid hardcoded values, pass a list in the arguments?
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                CharacterInfoFragment.newInstance()
            }
            1 -> {
                LocationsFragment.newInstance(characterId)
            }
            2 -> {
                EpisodesFragment.newInstance(characterId)
            }
            else -> LocationsFragment.newInstance(characterId)
        }
    }
}