package com.mobilechallenge.di

import com.mobilechallenge.core.data.repository.MovieRemoteRepository
import com.mobilechallenge.core.data.repository.MovieRemoteRepositoryImpl
import com.mobilechallenge.core.network.api.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRemoteRepository(movieApi: MovieApi): MovieRemoteRepository {
        return MovieRemoteRepositoryImpl(movieApi)
    }
}