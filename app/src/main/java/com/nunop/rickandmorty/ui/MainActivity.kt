package com.nunop.rickandmorty.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nunop.rickandmorty.R
import com.nunop.rickandmorty.api.RetrofitInstance
import com.nunop.rickandmorty.data.database.Database
import com.nunop.rickandmorty.data.database.LocationDao
import com.nunop.rickandmorty.data.paging.LocationsPagingDataSource
import com.nunop.rickandmorty.databinding.ActivityMainBinding
import com.nunop.rickandmorty.repository.character.CharacterRepositoryImpl
import com.nunop.rickandmorty.repository.episode.EpisodeRepositoryImpl
import com.nunop.rickandmorty.repository.location.LocationRepositoryImpl
import com.nunop.rickandmorty.ui.characters.CharactersViewModel
import com.nunop.rickandmorty.ui.characters.CharactersViewModelProviderFactory
import com.nunop.rickandmorty.ui.episodes.EpisodesViewModel
import com.nunop.rickandmorty.ui.episodes.EpisodesViewModelProviderFactory
import com.nunop.rickandmorty.ui.locations.LocationsViewModel
import com.nunop.rickandmorty.ui.locations.LocationsViewModelProviderFactory

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    lateinit var mCharactersViewModel: CharactersViewModel
    lateinit var mLocationsViewModel: LocationsViewModel
    lateinit var mEpisodesViewModel: EpisodesViewModel

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val databaseInstance = Database.getInstance(this)
        val daoLocation = databaseInstance.locationDao


        characterViewModel()


        episodeViewModel()


        locationViewModel(databaseInstance, daoLocation)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

    private fun locationViewModel(
        databaseInstance: Database,
        daoLocation: LocationDao
    ) {
        val repositoryLocation = LocationRepositoryImpl(
            databaseInstance.locationDao,
            LocationsPagingDataSource(daoLocation)
        )
        val viewModelLocationsProviderFactory =
            LocationsViewModelProviderFactory(repositoryLocation)
        mLocationsViewModel =
            ViewModelProvider(
                this,
                viewModelLocationsProviderFactory
            )[LocationsViewModel::class.java]
    }

    private fun episodeViewModel() {
        val repositoryEpisode = EpisodeRepositoryImpl(
            RetrofitInstance.api, Database.getInstance
                (this)
        )
        val viewModelEpisodesProviderFactory = EpisodesViewModelProviderFactory(repositoryEpisode)
        mEpisodesViewModel =
            ViewModelProvider(this, viewModelEpisodesProviderFactory)[EpisodesViewModel::class.java]
    }

    private fun characterViewModel() {
        val repositoryCharacter =
            CharacterRepositoryImpl(
                RetrofitInstance.api, Database.getInstance
                    (this)
            )
        val viewModelCharactersProviderFactory =
            CharactersViewModelProviderFactory(repositoryCharacter)
        mCharactersViewModel =
            ViewModelProvider(
                this,
                viewModelCharactersProviderFactory
            )[CharactersViewModel::class.java]
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.bottomNavFragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.characters, R.id.episodes, R.id.locations)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp()

    /**
     * Overriding popBackStack is necessary in this case if the app is started from the deep link.
     */
    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}