package com.nunop.rickandmorty.ui.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nunop.rickandmorty.databinding.CharacterDetailsFragmentBinding

class CharacterDetailsFragment : Fragment() {

    private var _binding: CharacterDetailsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}