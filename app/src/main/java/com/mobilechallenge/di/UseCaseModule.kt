package com.mobilechallenge.di

import com.mobilechallenge.core.data.repository.MovieRemoteRepository
import com.mobilechallenge.core.domain.GetNowPlayingMoviesUseCase
import com.mobilechallenge.core.domain.GetPopularMoviesUseCase
import com.mobilechallenge.core.model.data.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetUpcomingMovieUseCase(
        movieRemoteRepository: MovieRemoteRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(movieRemoteRepository, dispatcher)
    }

    @Provides
    @Singleton
    fun provideGetNowPlayingMoviesUseCase(
        movieRemoteRepository: MovieRemoteRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): GetNowPlayingMoviesUseCase {
        return GetNowPlayingMoviesUseCase(movieRemoteRepository, dispatcher)
    }
}