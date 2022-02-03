package com.nunop.rickandmorty.ui.episode.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.databinding.EpisodesFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
class EpisodesFragment : BaseFragment(), EpisodeAdapter.OnEpisodeClickListener {
    //Uses paging 3 and saves on the DB and uses it using a remote mediator that handles data
    // from api and db
    private var _binding: EpisodesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: EpisodesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as MainActivity).mEpisodesViewModel


        val adapter = EpisodeAdapter(this)

        binding.episodesList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )

        launchOnLifecycleScope {
            mViewModel.episodesFlow.collectLatest {
                adapter.submitData(it)
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
}