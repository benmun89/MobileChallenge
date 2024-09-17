package com.mobilechallenge.core.data.repository


import com.mobilechallenge.core.model.data.MovieResponseModel
import com.mobilechallenge.core.model.data.mapper.toDomainModel
import com.mobilechallenge.core.network.api.MovieApi
import retrofit2.await
import javax.inject.Inject

class MovieRemoteRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieRemoteRepository {

    override suspend fun getNowPlayingMovie(page: Int):  MovieResponseModel =
        movieApi.getNowPlayingMovie(page).await().toDomainModel()


    override suspend fun getMostPopularMovie(page: Int): MovieResponseModel =
        movieApi.getMostPopularMovie(page).await().toDomainModel()
}

