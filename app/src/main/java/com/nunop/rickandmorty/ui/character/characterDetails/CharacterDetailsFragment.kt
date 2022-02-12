package com.nunop.rickandmorty.ui.character.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.databinding.CharacterDetailsFragmentBinding
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.repository.character.CharacterRepositoryImpl
import com.nunop.rickandmorty.utils.Resource

class CharacterDetailsFragment : BaseFragment() {

    private lateinit var mCharacterDetailsViewModel: CharacterDetailsViewModel

    private var _binding: CharacterDetailsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterDetailsFragmentBinding.inflate(inflater, container, false)

        val databaseInstance = Database.getInstance(requireContext())
        val remoteDataSource = RemoteDataSource(RetrofitInstance.api)
        val localDataSource = LocalDataSource(databaseInstance)
        val repositoryCharacter =
            CharacterRepositoryImpl(remoteDataSource, localDataSource)


        val args: CharacterDetailsFragmentArgs by navArgs()
        val characterId = args.characterId


        characterDetailsViewModel(repositoryCharacter)


        launchOnLifecycleScope {
            mCharacterDetailsViewModel.getCharacterById(characterId)
        }

        mCharacterDetailsViewModel.characterLiveData.observe(viewLifecycleOwner) { response ->

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

    private fun characterDetailsViewModel(repositoryCharacter: CharacterRepository) {
        activity?.application?.let {
            val viewModelCharacterDetailsProviderFactory =
                CharacterDetailsViewModelProviderFactory(it, repositoryCharacter)
            mCharacterDetailsViewModel =
                ViewModelProvider(
                    this,
                    viewModelCharacterDetailsProviderFactory
                )[CharacterDetailsViewModel::class.java]
        }
    }

}