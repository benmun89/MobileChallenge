package com.mobilechallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mobilechallenge.core.data.repository.MovieLocalRepository
import com.mobilechallenge.core.model.data.MovieModel
import com.mobilechallenge.ui.details.DetailsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: DetailsViewModel
    private val movieLocalRepository: MovieLocalRepository = mock(MovieLocalRepository::class.java)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailsViewModel(movieLocalRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectMovie should update selectedMovie and check if it's favorite`() = runTest {
        val movie = MovieModel(
            adult = false,
            backdropPath = null,
            genreIds = emptyList(),
            id = 549566,
            originalLanguage = "en",
            originalTitle = "The Harry Potter Saga Analyzed",
            overview = "A complete deconstruction of the cultural impact and themes of the Harry Potter series in its 20 years of existence.",
            popularity = 0.981,
            posterPath = null,
            releaseDate = "",
            title = "The Harry Potter Saga Analyzed",
            video = false,
            voteAverage = 8.2,
            voteCount = 3, primaryKey = 1
        )

        val movieObserver = mock(Observer::class.java) as Observer<MovieModel>
        val favoriteObserver = mock(Observer::class.java) as Observer<Boolean>

        viewModel.selectedMovie.observeForever(movieObserver)
        viewModel.isFavorite.observeForever(favoriteObserver)

        // Mock repository response
        `when`(movieLocalRepository.isMovieInDatabase(movie.id)).thenReturn(false)

        viewModel.selectMovie(movie)
        verify(movieObserver).onChanged(movie)
        verify(favoriteObserver).onChanged(false)
    }
}
