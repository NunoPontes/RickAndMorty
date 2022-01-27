package com.nunop.rickandmorty.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.databinding.LocationsFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@ExperimentalPagingApi
class LocationsFragment : Fragment() {
    //Uses paging 3 and saves on the DB (but does not use that data)
    private var _binding: LocationsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: LocationsViewModel

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

        mViewModel.allLocations.observe(viewLifecycleOwner) { characters ->
            Timber.d(characters.toString()) //TODO: check
        }

        val adapter = LocationAdapter(context)

        launchOnLifecycleScope {
            mViewModel.locationsFlow.collectLatest { adapter.submitData(it) }
        }

        binding.locationsList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )
    }

    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }
}