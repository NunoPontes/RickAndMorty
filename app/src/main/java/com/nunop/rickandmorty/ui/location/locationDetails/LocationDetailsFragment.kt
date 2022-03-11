package com.nunop.rickandmorty.ui.location.locationDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.databinding.LocationDetailsFragmentBinding
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.repository.location.LocationRepository
import com.nunop.rickandmorty.repository.location.LocationRepositoryImpl
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.toVisibilityGone

class LocationDetailsFragment : BaseFragment() {

    private lateinit var mLocationDetailsViewModel: LocationDetailsViewModel

    private var _binding: LocationDetailsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationDetailsFragmentBinding.inflate(inflater, container, false)

        val databaseInstance = Database.getInstance(requireContext())
        val remoteDataSource = RemoteDataSource(RetrofitInstance.api)
        val localDataSource = LocalDataSource(databaseInstance)
        val repositoryLocation = LocationRepositoryImpl(
            localDataSource,
            remoteDataSource
        )

        val args: LocationDetailsFragmentArgs by navArgs()
        val locationId = args.locationId


        locationDetailsViewModel(repositoryLocation)


        launchOnLifecycleScope {
            mLocationDetailsViewModel.getLocationById(locationId)
        }

        mLocationDetailsViewModel.locationLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.textView2.text = response.data?.name
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

    private fun locationDetailsViewModel(repositoryLocation: LocationRepository) {
        activity?.application?.let {
            val viewModelLocationDetailsProviderFactory =
                LocationDetailsViewModelProviderFactory(it, repositoryLocation)
            mLocationDetailsViewModel =
                ViewModelProvider(
                    this,
                    viewModelLocationDetailsProviderFactory
                )[LocationDetailsViewModel::class.java]
        }
    }

    private fun showLoading(show: Boolean) {
        binding.ltMorty.visibility = show.toVisibilityGone()
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.ltGenericError.visibility = show.toVisibilityGone()
    }
}