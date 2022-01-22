package com.nunop.rickandmorty.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.databinding.LocationsFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.ui.characters.CharactersViewModel

@ExperimentalPagingApi
class LocationsFragment : Fragment() {

    private var _binding: LocationsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: CharactersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as MainActivity).mCharactersViewModel
    }
}