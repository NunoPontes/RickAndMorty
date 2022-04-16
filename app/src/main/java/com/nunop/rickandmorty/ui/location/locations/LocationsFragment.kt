package com.nunop.rickandmorty.ui.location.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.databinding.LocationsFragmentBinding
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import com.nunop.rickandmorty.utils.Utilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
@AndroidEntryPoint
class LocationsFragment : BaseFragment(), LocationAdapter.OnLocationClickListener {
    //Uses paging 3 and saves on the DB (but does not use that data)
    private var _binding: LocationsFragmentBinding? = null
    private val binding get() = _binding!!

    private val utilities = Utilities()
    private lateinit var mLocationsViewModel: LocationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLocationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]

        val adapter = LocationAdapter(this)

        collectFlowLocations(adapter)
        collectLoadStates(adapter)

        binding.locationsList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )

        binding.swipeLocations.apply {
            setOnRefreshListener {
                adapter.refresh()
                this.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.locationsList.adapter = null
        _binding = null
    }

    override fun onLocationClick(location: Location) {
        location.id?.let {
            findNavController().navigate(
                LocationsFragmentDirections.actionLocationsFragmentToLocationDetailsFragment
                    (locationId = it)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun collectLoadStates(adapter: LocationAdapter) {
        launchOnLifecycleScope {
            adapter.loadStateFlow.collect { loadStates ->
                val hasInternetConnection = context?.let { utilities.hasInternetConnection(it) }
                val error = (loadStates.refresh as? LoadState.Error)?.error
                utilities.checkStates(
                    error = error,
                    adapter = adapter as PagingDataAdapter<Any, RecyclerView.ViewHolder>,
                    hasInternetConnection = hasInternetConnection,
                    loadStates = loadStates,
                    showLoading = ::showLoading,
                    showErrorGeneric = ::showErrorGeneric,
                    showErrorNoInternet = ::showErrorNoInternet
                )
            }
        }
    }

    private fun collectFlowLocations(adapter: LocationAdapter) {
        launchOnLifecycleScope {
            mLocationsViewModel.locationsFlow.collectLatest {
                showLoading(false)
                showErrorGeneric(false)
                showErrorNoInternet(false)
                adapter.submitData(it)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.customError.showLoading(show)
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.customError.showErrorGeneric(show)
    }

    private fun showErrorNoInternet(show: Boolean) {
        binding.customError.showErrorNoInternet(show)
    }
}