package com.nunop.rickandmorty.ui.character.characterDetails.characterInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.databinding.CharacterInfoFragmentBinding
import com.nunop.rickandmorty.ui.character.characterDetails.CharacterDetailsViewModel
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.toVisibilityGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterInfoFragment : BaseFragment() {

    private val mCharacterDetailsViewModel: CharacterDetailsViewModel by activityViewModels()

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

    private fun setupViewModel() {
        mCharacterDetailsViewModel.characterLiveData.observe(viewLifecycleOwner) { response ->
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
    }

    private fun showLoading(show: Boolean) {
        binding.ltMortyInfo.visibility = show.toVisibilityGone()
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.ltGenericErrorInfo.visibility = show.toVisibilityGone()
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