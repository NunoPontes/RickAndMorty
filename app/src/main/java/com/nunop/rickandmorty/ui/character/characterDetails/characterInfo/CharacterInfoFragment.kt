package com.nunop.rickandmorty.ui.character.characterDetails.characterInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.databinding.CharacterInfoFragmentBinding
import com.nunop.rickandmorty.ui.character.characterDetails.CharacterDetailsViewModel
import com.nunop.rickandmorty.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterInfoFragment : BaseFragment() {

    private lateinit var mCharacterDetailsViewModel: CharacterDetailsViewModel

    private var _binding: CharacterInfoFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): CharacterInfoFragment {
            return CharacterInfoFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterInfoFragmentBinding.inflate(inflater, container, false)

        setupViewModel()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun characterDetailsViewModel() {
        activity?.let {
            mCharacterDetailsViewModel =
                ViewModelProvider(
                    it
                )[CharacterDetailsViewModel::class.java]
        }
    }

    private fun setupViewModel() {
        characterDetailsViewModel()

        mCharacterDetailsViewModel.characterLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    initializeViews(response)
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

    private fun initializeViews(response: Resource<Character>) {
        binding.apply {
            tvCharacterName.text = response.data?.name
            tvGender.text = response.data?.gender
            tvLocation.text = response.data?.location?.name
            tvOrigin.text = response.data?.origin?.name
            tvSpecies.text = response.data?.species
            tvStatus.text = response.data?.status
            tvType.text = response.data?.type
        }
    }
}