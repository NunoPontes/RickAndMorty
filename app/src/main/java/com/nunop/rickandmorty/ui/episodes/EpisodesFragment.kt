package com.nunop.rickandmorty.ui.episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.databinding.EpisodesFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.ui.characters.CharacterAdapter
import com.nunop.rickandmorty.ui.characters.CharactersViewModel
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
class EpisodesFragment : Fragment() {
    //Uses paging 3 and saves on the DB and uses it using a remote meidator that handles data
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


        val adapter = EpisodeAdapter(context)

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

    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }
}