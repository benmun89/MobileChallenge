package com.mobilechallenge.core.data.repository

import com.mobilechallenge.core.model.data.MovieResponseModel

interface MovieRemoteRepository {
    suspend fun getNowPlayingMovie(page: Int): MovieResponseModel
    suspend fun getMostPopularMovie(page: Int): MovieResponseModel
}