package com.mobilechallenge.di

import android.content.Context
import androidx.room.Room
import com.mobilechallenge.core.database.MovieDatabase
import com.mobilechallenge.core.database.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(context, MovieDatabase::class.java, MOVIE_TABLE_NAME)
            .build()

    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.movieDao()
    }

    companion object {
        private const val MOVIE_TABLE_NAME = "movie.db"
    }
}