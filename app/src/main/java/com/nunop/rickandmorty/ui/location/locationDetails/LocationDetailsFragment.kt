package com.nunop.rickandmorty.ui.location.locationDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.databinding.LocationDetailsFragmentBinding
import com.nunop.rickandmorty.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : BaseFragment() {

    private val mLocationDetailsViewModel: LocationDetailsViewModel by viewModels()

    private var _binding: LocationDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationDetailsFragmentBinding.inflate(inflater, container, false)

        binding.locationDetailsComposeView.setContent {
            RickAndMortyTheme {
                LocationDetailsScreen()
            }
        }

        val args: LocationDetailsFragmentArgs by navArgs()
        val locationId = args.locationId

        launchOnLifecycleScope {
            mLocationDetailsViewModel.getLocationById(locationId)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}