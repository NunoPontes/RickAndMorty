package com.nunop.rickandmorty.ui.location.locationDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.databinding.LocationDetailsFragmentBinding
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.toVisibilityGone
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

        val args: LocationDetailsFragmentArgs by navArgs()
        val locationId = args.locationId

        launchOnLifecycleScope {
            mLocationDetailsViewModel.getLocationById(locationId)
        }

        mLocationDetailsViewModel.locationLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    initializeViews(response)
                    showLoading(false)
                    showErrorGeneric(false)
                }
                is Resource.Error -> {
                    showLoading(false)
                    showErrorGeneric(true)
                }
                is Resource.Loading -> {
                    showErrorGeneric(false)
                    showLoading(true)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(show: Boolean) {
        binding.ltMorty.visibility = show.toVisibilityGone()
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.ltGenericError.visibility = show.toVisibilityGone()
    }

    private fun initializeViews(response: Resource<Location>) {
        binding.apply {
            tvName.text = response.data?.name
            tvType.text = response.data?.type
            tvCreated.text = response.data?.created
            tvDimension.text = response.data?.dimension
        }
    }
}