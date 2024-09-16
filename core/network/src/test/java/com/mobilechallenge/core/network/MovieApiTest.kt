package com.mobilechallenge.core.network

import com.mobilechallenge.core.model.data.DatesDto
import com.mobilechallenge.core.model.data.MovieDto
import com.mobilechallenge.core.model.data.MovieResponseDto
import com.mobilechallenge.core.network.api.MovieApi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

class MovieApiTest {
    @Mock
    private lateinit var movieApi: MovieApi

    @Mock
    private lateinit var callNowPlaying: Call<MovieResponseDto>

    @Mock
    private lateinit var callPopular: Call<MovieResponseDto>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private fun createSampleMovieResponseDto(): MovieResponseDto {
        return MovieResponseDto(
            dates = DatesDto(
                maximum = "2022-06-14",
                minimum = "2022-05-26"
            ),
            page = 1,
            results = listOf(
                MovieDto(
                    id = 799876,
                    title = "The Outfit",
                    backdropPath = "/jIdZmqElYgNwlCsUtCwmN1rDu7I.jpg",
                    posterPath = "/lZa5EB6PVJBT5mxhgZS5ftqdAm6.jpg",
                    overview = "Leonard is an English tailor who used to craft suits on London’s world-famous Savile Row. After a personal tragedy, he’s ended up in Chicago, operating a small tailor shop in a rough part of town where he makes beautiful clothes for the only people around who can afford them: a family of vicious gangsters.",
                    releaseDate = "2022-02-25",
                    voteAverage = 7.0,
                    voteCount = 212,
                    popularity = 1433.895,
                    originalLanguage = "en",
                    originalTitle = "The Outfit",
                    adult = false,
                    genreIds = listOf(80, 18, 53),
                    video = false
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
    }

    @Test
    fun testGetNowPlayingMovie() {
        val page = 1
        val movieResponseDto = createSampleMovieResponseDto()

        `when`(movieApi.getNowPlayingMovie(page)).thenReturn(callNowPlaying)
        `when`(callNowPlaying.execute()).thenReturn(Response.success(movieResponseDto))

        val response = movieApi.getNowPlayingMovie(page).execute()

        verify(movieApi).getNowPlayingMovie(page)
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(movieResponseDto, response.body())
    }

    @Test
    fun testGetMostPopularMovie() {
        val page = 1
        val movieResponseDto = createSampleMovieResponseDto()

        `when`(movieApi.getMostPopularMovie(page)).thenReturn(callPopular)
        `when`(callPopular.execute()).thenReturn(Response.success(movieResponseDto))

        val response = movieApi.getMostPopularMovie(page).execute()

        verify(movieApi).getMostPopularMovie(page)
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(movieResponseDto, response.body())
    }
}
