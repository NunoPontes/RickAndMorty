package com.nunop.rickandmorty.ui.character.characterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nunop.rickandmorty.R
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.databinding.CharacterDetailsFragmentBinding
import com.nunop.rickandmorty.datasource.localdatasource.LocalDataSource
import com.nunop.rickandmorty.datasource.remotedatasource.RemoteDataSource
import com.nunop.rickandmorty.repository.character.CharacterRepository
import com.nunop.rickandmorty.repository.character.CharacterRepositoryImpl
import com.nunop.rickandmorty.utils.Resource

@ExperimentalPagingApi
class CharacterDetailsFragment : BaseFragment() {

    private lateinit var mCharacterDetailsViewModel: CharacterDetailsViewModel

    private var _binding: CharacterDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private var myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout)
        }
    }

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

        setTabs()

        launchOnLifecycleScope {
            mCharacterDetailsViewModel.getCharacterById(characterId)
        }

        mCharacterDetailsViewModel.characterLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.apply {
                        context?.let {
                            Glide.with(it)
                                .load(response.data?.image)
                                .into(ivPhoto)
                        }
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tabViewpager.adapter = null
        binding.tabViewpager.unregisterOnPageChangeCallback(myPageChangeCallback)
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

    private fun setTabs() {
        val tabAdapter =
            activity?.let { TabAdapter(it) }
        binding.tabViewpager.adapter = tabAdapter
        binding.tabViewpager.isSaveEnabled = false
        binding.tabViewpager.registerOnPageChangeCallback(myPageChangeCallback)

        TabLayoutMediator(binding.tabLayout, binding.tabViewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.ic_people_white_18dp)
                }
            }
        }.attach()
    }
}