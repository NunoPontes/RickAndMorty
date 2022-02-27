package com.nunop.rickandmorty.ui.character.characterDetails.characterInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.databinding.CharacterInfoFragmentBinding

class CharacterInfoFragment : BaseFragment() {

    private var _binding: CharacterInfoFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): CharacterInfoFragment {
            return CharacterInfoFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterInfoFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}