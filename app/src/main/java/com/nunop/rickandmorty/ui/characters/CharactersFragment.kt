package com.nunop.rickandmorty.ui.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.nunop.rickandmorty.databinding.CharactersFragmentBinding
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.PagingLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
class CharactersFragment : Fragment() {
//Uses paging 3 and saves on the DB (but does not use that data)
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

        mViewModel.allCharacters.observe(viewLifecycleOwner) { characters ->
            Log.d("Teste", characters.toString())
        }

        val adapter = CharacterAdapter(context)

        launchOnLifecycleScope {
            mViewModel.charactersFlow.collectLatest { adapter.submitData(it) }
        }

        binding.charactersList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter),
            footer = PagingLoadStateAdapter(adapter)
        )

    }

    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }

}