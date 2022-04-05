package com.nunop.rickandmorty.ui.character.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nunop.rickandmorty.R
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.databinding.CharactersFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import com.nunop.rickandmorty.utils.Utilities
import com.nunop.rickandmorty.utils.autoFitColumns
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest


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
        binding.customError.showLoading(show)
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.customError.showErrorGeneric(show)
    }

    private fun showErrorNoInternet(show: Boolean) {
        binding.customError.showErrorNoInternet(show)
    }

    private fun collectFlowCharacters(adapter: CharacterAdapter) {
        launchOnLifecycleScope {
            mViewModel.charactersFlow.collectLatest {
                showLoading(false)
                showErrorGeneric(false)
                showErrorNoInternet(false)
                adapter.submitData(it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun collectLoadStates(adapter: CharacterAdapter) {
        launchOnLifecycleScope {
            adapter.loadStateFlow.collect { loadStates ->
                val hasInternetConnection = context?.let { utilities.hasInternetConnection(it) }
                val error = (loadStates.mediator?.refresh as? LoadState.Error)?.error
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
}