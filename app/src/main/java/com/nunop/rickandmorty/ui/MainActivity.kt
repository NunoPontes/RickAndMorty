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
import com.nunop.rickandmorty.data.paging.CharactersPagingDataSource
import com.nunop.rickandmorty.databinding.ActivityMainBinding
import com.nunop.rickandmorty.repository.character.CharacterRepositoryImpl
import com.nunop.rickandmorty.repository.episode.EpisodeRepositoryImpl
import com.nunop.rickandmorty.ui.characters.CharactersViewModel
import com.nunop.rickandmorty.ui.characters.CharactersViewModelProviderFactory
import com.nunop.rickandmorty.ui.episodes.EpisodesViewModel
import com.nunop.rickandmorty.ui.episodes.EpisodesViewModelProviderFactory

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    lateinit var mCharactersViewModel: CharactersViewModel
    lateinit var mEpisodesViewModel: EpisodesViewModel

    private lateinit var binding: ActivityMainBinding
//    private var currentNavController: LiveData<NavController>? = null
//    private var appBarConfiguration: AppBarConfiguration? = null

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val databaseInstance = Database.getInstance(this)
        val dao = databaseInstance.characterDao

        val repositoryCharacter =
            CharacterRepositoryImpl(databaseInstance.characterDao, CharactersPagingDataSource(dao))
        val viewModelCharactersProviderFactory =
            CharactersViewModelProviderFactory(repositoryCharacter)
        mCharactersViewModel =
            ViewModelProvider(
                this,
                viewModelCharactersProviderFactory
            )[CharactersViewModel::class.java]


        val repositoryEpisode = EpisodeRepositoryImpl(
            RetrofitInstance.api, Database.getInstance
                (this)
        )
        val viewModelEpisodesProviderFactory = EpisodesViewModelProviderFactory(repositoryEpisode)
        mEpisodesViewModel =
            ViewModelProvider(this, viewModelEpisodesProviderFactory)[EpisodesViewModel::class.java]

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

//        val characters = listOf(
//            Character(
//                id = 1,
//                name = "Ricky",
//                status = "Alive",
//                originLocationId = 1,
//                currentLocationId = 2
//            ),
//            Character(
//                id = 2,
//                name = "Morty",
//                status = "Alive",
//                originLocationId = 1,
//                currentLocationId = 2
//            ),
//            Character(
//                id = 3,
//                name = "Aquaman",
//                status = "Dead",
//                originLocationId = 3,
//                currentLocationId = 3
//            )
//        )
//
//        val locations = listOf(
//            Location(
//                id = 1,
//                name = "Earth",
//                type = "Grass"
//            ),
//            Location(
//                id = 2,
//                name = "Mars",
//                type = "Dirt"
//            ),
//            Location(
//                id = 3,
//                name = "Sea",
//                type = "Water"
//            )
//        )
//
//        val episodes = listOf(
//            Episode(
//                id = 1,
//                name = "S01E01",
//                air_date = "Grass"
//            ),
//            Episode(
//                id = 2,
//                name = "S01E02",
//                air_date = "Dirt"
//            ),
//            Episode(
//                id = 3,
//                name = "S01E03",
//                air_date = "Water"
//            )
//        )
//
//        val characterEpisodeRelations = listOf(
//            CharacterEpisodeCrossRef(
//                characterId = 1,
//                episodeId = 1
//            ),
//            CharacterEpisodeCrossRef(
//                characterId = 1,
//                episodeId = 2
//            ),
//            CharacterEpisodeCrossRef(
//                characterId = 1,
//                episodeId = 3
//            ),
//            CharacterEpisodeCrossRef(
//                characterId = 2,
//                episodeId = 1
//            ),
//            CharacterEpisodeCrossRef(
//                characterId = 2,
//                episodeId = 2
//            ),
//            CharacterEpisodeCrossRef(
//                characterId = 3,
//                episodeId = 1
//            )
//        )

//        lifecycleScope.launch {
//            characters.forEach { dao.insertCharacter(it) }
//            locations.forEach { dao.insertLocation(it) }
//            episodes.forEach { dao.insertEpisode(it) }
//            characterEpisodeRelations.forEach { dao.insertCharacterEpisodeCrossRef(it) }
//        }
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