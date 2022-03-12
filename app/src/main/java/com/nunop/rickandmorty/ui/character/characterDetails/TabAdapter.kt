package com.nunop.rickandmorty.ui.character.characterDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nunop.rickandmorty.ui.character.characterDetails.characterInfo.CharacterInfoFragment

@ExperimentalPagingApi
class TabAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    //TODO: find better way to avoid hardcoded values, pass a list in the arguments?
    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                CharacterInfoFragment.newInstance()
            }
            else -> CharacterInfoFragment.newInstance()
        }
    }
}