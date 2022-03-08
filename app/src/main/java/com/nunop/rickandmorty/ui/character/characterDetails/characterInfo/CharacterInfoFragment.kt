package com.nunop.rickandmorty.ui.character.characterDetails.characterInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.databinding.CharacterInfoFragmentBinding
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.repository.character.CharacterRepositoryImpl
import com.nunop.rickandmorty.ui.character.characterDetails.CharacterDetailsViewModel
import com.nunop.rickandmorty.ui.character.characterDetails.CharacterDetailsViewModelProviderFactory
import com.nunop.rickandmorty.utils.Resource

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
                        tvCharacterName.text = response.data?.name
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
}