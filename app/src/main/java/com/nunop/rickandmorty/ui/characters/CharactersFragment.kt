package com.nunop.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.R
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.databinding.CharactersFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import com.nunop.rickandmorty.utils.navigateSafe
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
class CharactersFragment : Fragment(), CharacterAdapter.OnCharacterClickListener {
    //Uses paging 3 and saves on the DB and uses it using a remote meidator that handles data
    // from api and db
    private var _binding: CharactersFragmentBinding? = null
    private val binding get() = _binding!!

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

        binding.charactersList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )

        launchOnLifecycleScope {
            mViewModel.charactersFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    fun launchOnLifecycleScope(execute: suspend () -> Unit) { //TODO: extract to avoid duplication
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }

    override fun onCharacterClick(character: Character) {
        val bundle = Bundle()
        character.id?.let {
            bundle.putInt(
                requireContext().getString(R.string.bundle_character_id),
                it
            )
        } //TODO use safeargs instead of bundle to pass the id
        //I could pass the whole object as a parcelable, but I'll pass
        //just the id, because I want to get more examples of MVVM with simple API requests
        view?.let {
            Navigation.findNavController(it)
                .navigateSafe(R.id.action_charactersFragment_to_characterDetailsFragment, bundle)
        }
    }

}