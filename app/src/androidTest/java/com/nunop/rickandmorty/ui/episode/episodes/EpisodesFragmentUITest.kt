package com.nunop.rickandmorty.ui.episode.episodes

import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nunop.rickandmorty.R
import com.nunop.rickandmorty.ui.MainActivity
import com.nunop.rickandmorty.utils.AndroidTestUtils
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
class EpisodesFragmentUITest {

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

        onView(ViewMatchers.withId(R.id.episodes)).perform(click())

        onView(ViewMatchers.withId(R.id.episodesList)).check(
            hasItemCount(
                20
            )
        )

        validateListContent(
            position = 0,
            episodeName = "Pilot",
            episodeAirDate = "December 2, 2013",
            episode = "S01E01"
        )

        validateListContent(
            position = 1,
            episodeName = "Lawnmower Dog",
            episodeAirDate = "December 9, 2013",
            episode = "S01E02"
        )

        validateListContent(
            position = 2,
            episodeName = "Anatomy Park",
            episodeAirDate = "December 16, 2013",
            episode = "S01E03"
        )
    }

    @Test
    fun successWhenNavigateToEpisodeDetails() {
        server.dispatcher = MockServerDispatcherAndroid().RequestDispatcher()

        onView(ViewMatchers.withId(R.id.episodes)).perform(click())

        onView(ViewMatchers.withId(R.id.episodesList)).check(
            hasItemCount(
                20
            )
        )

        validateListContent(
            position = 0,
            episodeName = "Pilot",
            episodeAirDate = "December 2, 2013",
            episode = "S01E01"
        )

        onView(ViewMatchers.withId(R.id.episodesList))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(ViewMatchers.withId(R.id.tvName))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("Pilot")))
        onView(ViewMatchers.withId(R.id.tvCreated))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("2017-11-10T12:56:33.798Z")))
        onView(ViewMatchers.withId(R.id.tvAirDate))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("December 2, 2013")))
        onView(ViewMatchers.withId(R.id.tvEpisode))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText("S01E01")))

        onView(ViewMatchers.withId(R.id.lt_morty)).check(matches(Matchers.not(isCompletelyDisplayed())))
        onView(ViewMatchers.withId(R.id.lt_generic_error)).check(matches(
            Matchers.not(
                isCompletelyDisplayed()
            )
        ))
    }

    private fun validateListContent(
        position: Int,
        episodeName: String?,
        episodeAirDate: String?,
        episode: String?
    ) {
        episodeName?.let {
            onView(
                AndroidTestUtils.withRecyclerView(R.id.episodesList).atPositionOnView(
                    position,
                    R.id.tvName
                )
            ).check(
                matches(withText(episodeName))
            )
        }

        episodeAirDate?.let {
            onView(
                AndroidTestUtils.withRecyclerView(R.id.episodesList).atPositionOnView(
                    position,
                    R.id.tvAirDate
                )
            ).check(
                matches(withText(episodeAirDate))
            )
        }

        episode?.let {
            onView(
                AndroidTestUtils.withRecyclerView(R.id.episodesList).atPositionOnView(
                    position,
                    R.id.tvEpisode
                )
            ).check(
                matches(withText(episode))
            )
        }
    }

    private fun hasItemCount(count: Int): ViewAssertion {
        return RecyclerViewItemCountAssertion(
            count
        )
    }
}