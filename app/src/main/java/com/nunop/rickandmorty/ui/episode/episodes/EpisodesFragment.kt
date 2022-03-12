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
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.databinding.EpisodesFragmentBinding
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.repository.character.CharacterRepositoryImpl
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.ui.character.characterDetails.CharacterDetailsViewModel
import com.nunop.rickandmorty.ui.character.characterDetails.CharacterDetailsViewModelProviderFactory
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.toVisibilityGone
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
class EpisodesFragment : BaseFragment(), EpisodeAdapter.OnEpisodeClickListener {
    //Uses paging 3 and saves on the DB and uses it using a remote mediator that handles data
    // from api and db
    private var _binding: EpisodesFragmentBinding? = null
    private val binding get() = _binding!!

    private val utilities = Utilities()
    private lateinit var mCharacterDetailsViewModel: CharacterDetailsViewModel
    private lateinit var mEpisodesViewModel: EpisodesViewModel
    //TODO: add error, loading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EpisodesFragmentBinding.inflate(inflater, container, false)
        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEpisodesViewModel = (activity as MainActivity).mEpisodesViewModel


        val adapter = EpisodeAdapter(this)

        binding.episodesList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )

        launchOnLifecycleScope {
            mEpisodesViewModel.episodesFlow.collectLatest {
                adapter.submitData(it)
            }
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

    private fun characterDetailsViewModel(repositoryCharacter: CharacterRepository) {
        activity?.let {
            val viewModelCharacterDetailsProviderFactory =
                CharacterDetailsViewModelProviderFactory(it.application, repositoryCharacter)
            mCharacterDetailsViewModel =
                ViewModelProvider(
                    it,
                    viewModelCharacterDetailsProviderFactory
                )[CharacterDetailsViewModel::class.java]
        }
    }

    private fun setupViewModel() {
        val databaseInstance = Database.getInstance(requireContext())
        val remoteDataSource = RemoteDataSource(RetrofitInstance.api)
        val localDataSource = LocalDataSource(databaseInstance)
        val repositoryCharacter =
            CharacterRepositoryImpl(remoteDataSource, localDataSource)

        characterDetailsViewModel(repositoryCharacter)

        mCharacterDetailsViewModel.characterLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.apply {
                        response.data?.id
                        //TODO: get from CharacterEpisodeCrossRef episodeId using characterId,
                    }
                }
                is Resource.Error -> {
                    //TODO:
                }
                is Resource.Loading -> {
                    //TODO:
                }
            }
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