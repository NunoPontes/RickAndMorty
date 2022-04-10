package com.nunop.rickandmorty.ui.episode.episodeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.databinding.EpisodeDetailsFragmentBinding
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.toVisibilityGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeDetailsFragment : BaseFragment() {

    private lateinit var mEpisodeDetailsViewModel: EpisodeDetailsViewModel

    private var _binding: EpisodeDetailsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodeDetailsFragmentBinding.inflate(inflater, container, false)

//        val databaseInstance = Database.getInstance(requireContext())
//        val remoteDataSource = RemoteDataSource(RetrofitInstance.api)
//        val localDataSource = LocalDataSource(databaseInstance)
//        val repositoryEpisode =
//            EpisodeRepositoryImpl(remoteDataSource, localDataSource)


        val args: EpisodeDetailsFragmentArgs by navArgs()
        val episodeId = args.episodeId


        characterDetailsViewModel()


        launchOnLifecycleScope {
            mEpisodeDetailsViewModel.getEpisodeById(episodeId)
        }

        mEpisodeDetailsViewModel.episodeLiveData.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    binding.textView2.text = response.data?.name
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

    private fun characterDetailsViewModel() {
        activity?.application?.let {
            mEpisodeDetailsViewModel =
                ViewModelProvider(
                    this
                )[EpisodeDetailsViewModel::class.java]
        }
    }

    private fun showLoading(show: Boolean) {
        binding.ltMorty.visibility = show.toVisibilityGone()
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.ltGenericError.visibility = show.toVisibilityGone()
    }
}