package com.mobilechallenge.di

import com.mobilechallenge.core.model.data.IoDispatcher
import com.mobilechallenge.core.model.data.MainImmediateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class CoroutinesModule {
    @IoDispatcher
    @Provides
    fun providesIoDispatcher() = Dispatchers.IO

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher() = Dispatchers.Main.immediate
}