package com.nunop.rickandmorty.ui.character.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.nunop.rickandmorty.R
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.databinding.CharactersFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import retrofit2.HttpException
import java.net.UnknownHostException


@ExperimentalPagingApi
class CharactersFragment : BaseFragment(), CharacterAdapter
.OnCharacterClickListener {
    //Uses paging 3 and saves on the DB and uses it using a remote mediator that handles data
    // from api and db
    private var _binding: CharactersFragmentBinding? = null
    private val binding get() = _binding!!

    private val utilities = Utilities()

    private lateinit var mViewModel: CharactersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as MainActivity).mCharactersViewModel

        val adapter = CharacterAdapter(context, this)
        collectLoadStates(adapter)

        val columnWidth =
            (resources.getDimension(R.dimen.image_size) / resources.displayMetrics.density).toInt()
        val marginWidth =
            (resources.getDimension(R.dimen.margin_normal) / resources.displayMetrics.density).toInt()
        binding.charactersList.autoFitColumns(columnWidth + marginWidth)
        binding.charactersList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )

        binding.swipeCharacters.apply {
            setOnRefreshListener {
                adapter.refresh()
                this.isRefreshing = false
            }
        }

        collectFlowCharacters(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.charactersList.adapter = null
        _binding = null
    }

    override fun onCharacterClick(character: Character) {
        //I could pass the whole object as a parcelable, but I'll pass
        //just the id, because I want to get more examples of MVVM with simple API requests
        character.id?.let {
            findNavController().navigate(
                CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailsFragment(
                    characterId = it
                )
            )
        }
    }

    private fun showLoading(show: Boolean) {
        binding.ltMorty.visibility = show.toVisibilityGone()
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.ltGenericError.visibility = show.toVisibilityGone()
    }

    private fun showErrorNoInternet(show: Boolean) {
        binding.ltNoInternet.visibility = show.toVisibilityGone()
    }

    private fun collectFlowCharacters(adapter: CharacterAdapter) {
        //TODO: remove?
        launchOnLifecycleScope {
            when (mViewModel.charactersFlow) {
                is Resource.Success -> {
                    mViewModel.charactersFlow.data?.collectLatest {
                        showLoading(false)
                        showErrorGeneric(false)
                        showErrorNoInternet(false)
                        adapter.submitData(it)
                    }
                }
//                is Resource.Error -> {
//                    showLoading(false)
//                    showErrorGeneric(true)
//                    showErrorNoInternet(false)
//                }
//                is Resource.Loading -> {
//                    showErrorGeneric(false)
//                    showErrorNoInternet(false)
//                    showLoading(true)
//                }
            }
        }
    }

    private fun collectLoadStates(adapter: CharacterAdapter) {
        launchOnLifecycleScope {
            adapter.loadStateFlow.collect { loadStates ->
                val hasInternetConnection = context?.let { utilities.hasInternetConnection(it) }
                val error = (loadStates.mediator?.refresh as? LoadState.Error)?.error
                if ((error is HttpException || error is UnknownHostException) &&
                    adapter.snapshot().items.isEmpty() &&
                    hasInternetConnection == false
                ) {
                    showLoading(false)
                    showErrorNoInternet(true)
                    showErrorGeneric(false)
                } else if (error is Exception &&
                    adapter.snapshot().items.isEmpty()
                ) {
                    showLoading(false)
                    showErrorGeneric(true)
                    showErrorNoInternet(false)
                } else if (loadStates.mediator?.refresh is LoadState.Loading) {
                    showErrorGeneric(false)
                    showErrorNoInternet(false)
                    showLoading(true)
                } else if (loadStates.mediator?.refresh is LoadState.NotLoading) {
                    showErrorGeneric(false)
                    showErrorNoInternet(false)
                    showLoading(false)
                } else {
                    showLoading(false)
                    showErrorGeneric(false)
                    showErrorNoInternet(false)
                }
            }
        }
    }
}