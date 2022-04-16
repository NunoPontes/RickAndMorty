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
import com.nunop.rickandmorty.base.BaseFragment
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.databinding.CharacterDetailsFragmentBinding
import com.nunop.rickandmorty.utils.Resource
import com.nunop.rickandmorty.utils.toVisibilityGone
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
@AndroidEntryPoint
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

        val args: CharacterDetailsFragmentArgs by navArgs()
        val characterId = args.characterId

        characterDetailsViewModel()

        setTabs()

        launchOnLifecycleScope {
            mCharacterDetailsViewModel.getCharacterById(characterId)
        }

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tabViewpager.adapter = null
        binding.tabViewpager.unregisterOnPageChangeCallback(myPageChangeCallback)
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

    private fun showLoading(show: Boolean) {
        binding.ltMortyDetails.visibility = show.toVisibilityGone()
    }

    private fun showErrorGeneric(show: Boolean) {
        binding.ltGenericErrorDetails.visibility = show.toVisibilityGone()
    }

    private fun initializeViews(response: Resource<Character>) {
        binding.apply {
            context?.let {
                Glide.with(it)
                    .load(response.data?.image)
                    .into(ivPhoto)
            }
        }
    }
}