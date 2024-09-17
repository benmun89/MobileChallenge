package com.mobilechallenge.core.data.repository

import androidx.paging.PagingSource
import com.mobilechallenge.core.database.model.MovieEntity
import com.mobilechallenge.core.model.data.MovieModel

interface MovieLocalRepository {
    suspend fun replaceMovies(movieModels: List<MovieModel>)
    suspend fun deleteAll()
    suspend fun insertMovie(movieModel: MovieModel)
    suspend fun deleteMovie(id: Long)
    suspend fun isMovieInDatabase(id: Long): Boolean
    fun getAllMovies(): PagingSource<Int, MovieEntity>
    fun getAllMoviesOrderByName(): PagingSource<Int, MovieEntity>
    fun getAllMoviesOrderByDate(): PagingSource<Int, MovieEntity>
}
