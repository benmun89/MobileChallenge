package com.mobilechallenge.core.network.api

import com.mobilechallenge.core.model.data.MovieResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("3/movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page: Int): Call<MovieResponseDto>

    @GET("3/movie/popular")
    fun getMostPopularMovie(@Query("page") page: Int): Call<MovieResponseDto>
}