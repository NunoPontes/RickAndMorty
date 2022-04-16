package com.nunop.rickandmorty.ui.episode.episodes

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
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.databinding.EpisodesFragmentBinding
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import com.nunop.rickandmorty.utils.Utilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
@AndroidEntryPoint
class EpisodesFragment : BaseFragment(), EpisodeAdapter.OnEpisodeClickListener {
    //Uses paging 3 and saves on the DB and uses it using a remote mediator that handles data
    // from api and db
    private var _binding: EpisodesFragmentBinding? = null
    private val binding get() = _binding!!

    private val utilities = Utilities()
    private lateinit var mEpisodesViewModel: EpisodesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodesFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEpisodesViewModel = ViewModelProvider(this)[EpisodesViewModel::class.java]


        val adapter = EpisodeAdapter(this)

        binding.episodesList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )

        collectFlowEpisodes(adapter)
        collectLoadStates(adapter)

        binding.swipeEpisodes.apply {
            setOnRefreshListener {
                adapter.refresh()
                this.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.episodesList.adapter = null
        _binding = null
    }

    override fun onEpisodeClick(episode: Episode) {
        episode.id?.let {
            findNavController().navigate(
                EpisodesFragmentDirections.actionEpisodesFragmentToEpisodeDetailsFragment
                    (episodeId = it)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun collectLoadStates(adapter: EpisodeAdapter) {
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

    private fun collectFlowEpisodes(adapter: EpisodeAdapter) {
        launchOnLifecycleScope {
            mEpisodesViewModel.episodesFlow.collectLatest {
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