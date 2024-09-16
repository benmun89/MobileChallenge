package com.mobilechallenge.ui.main

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobilechallenge.core.domain.GetNowPlayingMoviesUseCase
import com.mobilechallenge.core.model.data.MovieModel

class NowPlayingMoviePagingSource(
    private val getPopularMoviesUseCase: GetNowPlayingMoviesUseCase
) : PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val page = params.key ?: 1
        Log.d("MovieDataSource", "Loading page: $page")
        return try {
            val response = getPopularMoviesUseCase.execute(page)
            Log.d("MovieDataSource", "Loaded ${response.movies.size} items")
            LoadResult.Page(
                data = response.movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.page >= response.totalPages) null else page + 1
            )
        } catch (exception: Exception) {
            Log.e("MovieDataSource", "Error loading page: $page", exception)
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}