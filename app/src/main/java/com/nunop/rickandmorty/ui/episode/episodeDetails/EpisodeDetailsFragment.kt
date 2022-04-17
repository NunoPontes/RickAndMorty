package com.nunop.rickandmorty.ui.episode.episodeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.databinding.EpisodeDetailsFragmentBinding
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.toVisibilityGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeDetailsFragment : BaseFragment() {

    private val mEpisodeDetailsViewModel: EpisodeDetailsViewModel by viewModels()

    private var _binding: EpisodeDetailsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodeDetailsFragmentBinding.inflate(inflater, container, false)

        val args: EpisodeDetailsFragmentArgs by navArgs()
        val episodeId = args.episodeId

        launchOnLifecycleScope {
            mEpisodeDetailsViewModel.getEpisodeById(episodeId)
        }

        mEpisodeDetailsViewModel.episodeLiveData.observe(viewLifecycleOwner) { response ->

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

    private fun initializeViews(response: Resource<Episode>) {
        binding.apply {
            tvName.text = response.data?.name
            tvAirDate.text = response.data?.air_date
            tvCreated.text = response.data?.created
            tvEpisode.text = response.data?.episode
        }
    }
}