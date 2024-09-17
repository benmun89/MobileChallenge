package com.mobilechallenge.di

import com.mobilechallenge.core.data.repository.MovieLocalRepository
import com.mobilechallenge.core.data.repository.MovieLocalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface LocalMovieModule {

    @Binds
    fun provideLocalRepository(localRepositoryImpl: MovieLocalRepositoryImpl): MovieLocalRepository
}