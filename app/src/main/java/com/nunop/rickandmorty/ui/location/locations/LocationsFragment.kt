package com.nunop.rickandmorty.ui.location.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.databinding.LocationsFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.toVisibilityGone
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
class LocationsFragment : BaseFragment(), LocationAdapter.OnLocationClickListener {
    //Uses paging 3 and saves on the DB (but does not use that data)
    private var _binding: LocationsFragmentBinding? = null
    private val binding get() = _binding!!

    private val utilities = Utilities()
    private lateinit var mViewModel: LocationsViewModel
    //TODO: add error, loading
    companion object {
        fun newInstance(characterId: Int): LocationsFragment {
            return LocationsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as MainActivity).mLocationsViewModel

        val adapter = LocationAdapter(this)

        launchOnLifecycleScope {
            mViewModel.locationsFlow.collectLatest { adapter.submitData(it) }
        }

        launchOnLifecycleScope {
            adapter.loadStateFlow.collect { loadStates->
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

    //TODO: extract this somewhere to avoid duplications
    private fun showLoading(show: Boolean) {
        binding.ltMorty.visibility = show.toVisibilityGone()
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.ltGenericError.visibility = show.toVisibilityGone()
    }

    private fun showErrorNoInternet(show: Boolean) {
        binding.ltNoInternet.visibility = show.toVisibilityGone()
    }
}