package com.mobilechallenge.core.data.repository

import androidx.paging.PagingSource
import com.mobilechallenge.core.database.dao.MovieDao
import com.mobilechallenge.core.database.model.MovieEntity
import com.mobilechallenge.core.model.data.MovieModel
import com.mobilechallenge.core.model.data.mapper.toDomainModel
import com.mobilechallenge.core.model.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieLocalRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieLocalRepository {
    override suspend fun replaceMovies(movieModels: List<MovieModel>) {
        movieDao.insertMovies(movieModels.map(MovieModel::toEntity))
    }
    override suspend fun deleteAll() {
        movieDao.deleteAll()
    }

    override suspend fun insertMovie(movieModel: MovieModel) {
       movieDao.insertMovie(movieModel.toEntity())
    }

    override fun getAllMovies(): PagingSource<Int, MovieEntity> {
        return movieDao.getAllMovies()
    }

    override fun getAllMoviesOrderByName(): PagingSource<Int, MovieEntity> {
        return movieDao.getAllMoviesOrderedByName()
    }

    override fun getAllMoviesOrderByDate(): PagingSource<Int, MovieEntity> {
        return movieDao.getAllMoviesOrderedByDate()
    }

    override suspend fun deleteMovie(id: Long) {
        return movieDao.deleteMovie(id)
    }

    override suspend fun isMovieInDatabase(id: Long): Boolean {
        return movieDao.isMovieInDatabase(id) > 0
    }
}