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
import com.nunop.rickandmorty.databinding.ActivityMainBinding
import com.nunop.rickandmorty.ui.character.characters.CharactersViewModel
import com.nunop.rickandmorty.ui.episode.episodes.EpisodesViewModel
import com.nunop.rickandmorty.ui.location.locations.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
@AndroidEntryPoint
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

        mLocationsViewModel =
            ViewModelProvider(this)[LocationsViewModel::class.java]

        mEpisodesViewModel =
            ViewModelProvider(this)[EpisodesViewModel::class.java]
        mCharactersViewModel = ViewModelProvider(this)[CharactersViewModel::class.java]

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
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