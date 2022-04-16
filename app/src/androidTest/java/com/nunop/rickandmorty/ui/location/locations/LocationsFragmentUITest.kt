package com.nunop.rickandmorty.ui.location.locations

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
import org.hamcrest.Matchers
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
class LocationsFragmentUITest {

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

        onView(withId(R.id.locations)).perform(click())

        onView(withId(R.id.locationsList)).check(
            hasItemCount(
                20
            )
        )

        validateListContent(
            position = 0,
            locationName = "Earth (C-137)",
            locationType = "Planet",
            locationDimension = "Dimension C-137"
        )

        validateListContent(
            position = 1,
            locationName = "Abadango",
            locationType = "Cluster",
            locationDimension = "unknown"
        )

        validateListContent(
            position = 2,
            locationName = "Citadel of Ricks",
            locationType = "Space station",
            locationDimension = "unknown"
        )
    }

    @Test
    fun successWhenNavigateToLocationDetails() {
        server.dispatcher = MockServerDispatcherAndroid().RequestDispatcher()

        onView(withId(R.id.locations)).perform(click())

        onView(withId(R.id.locationsList)).check(
            hasItemCount(
                20
            )
        )

        validateListContent(
            position = 0,
            locationName = "Earth (C-137)",
            locationType = "Planet",
            locationDimension = "Dimension C-137"
        )

        onView(withId(R.id.locationsList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(withId(R.id.tvName))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Earth (C-137)")))
        onView(withId(R.id.tvType))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Planet")))
        onView(withId(R.id.tvCreated))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("2017-11-10T12:42:04.162Z")))
        onView(withId(R.id.tvDimension))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Dimension C-137")))

        onView(withId(R.id.lt_morty)).check(matches(Matchers.not(isCompletelyDisplayed())))
        onView(withId(R.id.lt_generic_error)).check(matches(Matchers.not(isCompletelyDisplayed())))
    }

    private fun validateListContent(
        position: Int,
        locationName: String?,
        locationType: String?,
        locationDimension: String?
    ) {
        locationName?.let {
            onView(
                withRecyclerView(R.id.locationsList).atPositionOnView(
                    position,
                    R.id.tvName
                )
            ).check(
                matches(withText(locationName))
            )
        }

        locationType?.let {
            onView(
                withRecyclerView(R.id.locationsList).atPositionOnView(
                    position,
                    R.id.tvType
                )
            ).check(
                matches(withText(locationType))
            )
        }

        locationDimension?.let {
            onView(
                withRecyclerView(R.id.locationsList).atPositionOnView(
                    position,
                    R.id.tvDimension
                )
            ).check(
                matches(withText(locationDimension))
            )
        }

    }

    private fun hasItemCount(count: Int): ViewAssertion {
        return RecyclerViewItemCountAssertion(
            count
        )
    }
}