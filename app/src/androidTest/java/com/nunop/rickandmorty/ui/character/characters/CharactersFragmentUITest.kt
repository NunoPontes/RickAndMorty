package com.nunop.rickandmorty.ui.character.characters

import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nunop.rickandmorty.R
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.AndroidTestUtils.withRecyclerView
import com.nunop.rickandmorty.utils.MockServerDispatcherAndroid
import com.nunop.rickandmorty.utils.RecyclerViewItemCountAssertion
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith


@ExperimentalPagingApi
@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CharactersFragmentUITest {

    @get:Rule
    var rule: RuleChain? =
        RuleChain.outerRule(HiltAndroidRule(this)).around(activityScenarioRule<MainActivity>())

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start(9090)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun successWhenFirst2PagesSuccess() {
        server.dispatcher = MockServerDispatcherAndroid().RequestDispatcher()

        onView(withId(R.id.charactersList)).check(
            hasItemCount(
                40
            )
        )

        validateListContent(
            position = 0,
            characterName = "Rick Sanchez"
        )

        validateListContent(
            position = 1,
            characterName = "Morty Smith"
        )

        validateListContent(
            position = 2,
            characterName = "Summer Smith"
        )

        validateListContent(
            position = 3,
            characterName = "Beth Smith"
        )

        validateListContent(
            position = 4,
            characterName = "Jerry Smith"
        )

        validateListContent(
            position = 5,
            characterName = "Abadango Cluster Princess"
        )

        validateListContent(
            position = 6,
            characterName = "Abradolf Lincler"
        )

    }

    @Test
    fun successWhenNavigateToCharacterDetails() {
        server.dispatcher = MockServerDispatcherAndroid().RequestDispatcher()

        onView(withId(R.id.charactersList)).check(
            hasItemCount(
                40
            )
        )

        validateListContent(
            position = 0,
            characterName = "Rick Sanchez"
        )

        onView(withId(R.id.charactersList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.ivPhoto)).check(matches(isDisplayed()))
        onView(withId(R.id.tvCharacterName)).check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Rick Sanchez")))
        onView(withId(R.id.tvGender)).check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Male")))
        onView(withId(R.id.tvSpecies)).check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Human")))
        onView(withId(R.id.tvType)).check(matches(not(isCompletelyDisplayed())))
        onView(withId(R.id.tvOrigin)).check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Earth (C-137)")))
        onView(withId(R.id.tvLocation)).check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Citadel of Ricks")))
        onView(withId(R.id.tvStatus)).check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Alive")))

        onView(withId(R.id.lt_morty_details)).check(matches(not(isCompletelyDisplayed())))
        onView(withId(R.id.lt_generic_error_details)).check(matches(not(isCompletelyDisplayed())))
        onView(withId(R.id.lt_morty_info)).check(matches(not(isCompletelyDisplayed())))
        onView(withId(R.id.lt_generic_error_info)).check(matches(not(isCompletelyDisplayed())))
    }

    private fun validateListContent(
        position: Int,
        characterName: String?
    ) {
        characterName?.let {
            onView(
                withRecyclerView(R.id.charactersList).atPositionOnView(
                    position,
                    R.id.tvName
                )
            ).check(
                matches(withText(characterName))
            )
        }

    }

    private fun hasItemCount(count: Int): ViewAssertion {
        return RecyclerViewItemCountAssertion(
            count
        )
    }
}