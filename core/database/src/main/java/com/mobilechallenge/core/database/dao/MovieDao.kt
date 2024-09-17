package com.mobilechallenge.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobilechallenge.core.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie")
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Long): Flow<MovieEntity>

    @Query("DELETE FROM movie")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movie ORDER BY title ASC")
    fun getAllMoviesOrderedByName():  PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movie ORDER BY releaseDate ASC")
    fun getAllMoviesOrderedByDate():  PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun deleteMovie(id: Long)

    @Query("SELECT COUNT(*) FROM movie WHERE id = :id")
    suspend fun isMovieInDatabase(id: Long): Int
}