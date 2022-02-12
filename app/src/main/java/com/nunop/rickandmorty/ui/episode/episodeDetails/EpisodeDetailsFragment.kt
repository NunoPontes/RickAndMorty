package com.nunop.rickandmorty.ui.episode.episodeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.databinding.EpisodeDetailsFragmentBinding
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.repository.episode.EpisodeRepository
import com.nunop.rickandmorty.repository.episode.EpisodeRepositoryImpl
import com.nunop.rickandmorty.utils.Resource

class EpisodeDetailsFragment : BaseFragment() {

    private lateinit var mEpisodeDetailsViewModel: EpisodeDetailsViewModel

    private var _binding: EpisodeDetailsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodeDetailsFragmentBinding.inflate(inflater, container, false)

        val databaseInstance = Database.getInstance(requireContext())
        val remoteDataSource = RemoteDataSource(RetrofitInstance.api)
        val localDataSource = LocalDataSource(databaseInstance)
        val repositoryEpisode =
            EpisodeRepositoryImpl(remoteDataSource, localDataSource)


        val args: EpisodeDetailsFragmentArgs by navArgs()
        val episodeId = args.episodeId


        characterDetailsViewModel(repositoryEpisode)


        launchOnLifecycleScope {
            mEpisodeDetailsViewModel.getEpisodeById(episodeId)
        }

        mEpisodeDetailsViewModel.episodeLiveData.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Success -> {
                    binding.textView2.text = response.data?.name
                }
                is Resource.Error -> {
                    //TODO:
                }
                is Resource.Loading -> {
                    //TODO:
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun characterDetailsViewModel(repositoryCharacter: EpisodeRepository) {
        activity?.application?.let {
            val viewModelEpisodeDetailsProviderFactory =
                EpisodeDetailsViewModelProviderFactory(it, repositoryCharacter)
            mEpisodeDetailsViewModel =
                ViewModelProvider(
                    this,
                    viewModelEpisodeDetailsProviderFactory
                )[EpisodeDetailsViewModel::class.java]
        }
    }

}