package com.mobilechallenge

import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobilechallenge.ui.main.MovieListType
import com.mobilechallenge.ui.main.MovieViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var movieViewModelFactory: ViewModelProvider.Factory

    private lateinit var movieViewModel: MovieViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        activityScenarioRule.scenario.onActivity { activity ->
            movieViewModel = ViewModelProvider(activity, movieViewModelFactory)
                .get(MovieViewModel::class.java)
        }
    }

    @Test
    fun testLoadMoviesForCurrentType() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testMovieListTypeSwitch() {
        movieViewModel.setMovieListType(MovieListType.NOW_PLAYING)
        onView(withText(R.string.now_playing_toolbar)).check(matches(isDisplayed()))
    }
}
