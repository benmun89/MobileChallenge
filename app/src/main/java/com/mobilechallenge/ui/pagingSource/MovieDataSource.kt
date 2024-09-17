package com.mobilechallenge.ui.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobilechallenge.core.domain.GetPopularMoviesUseCase
import com.mobilechallenge.core.model.data.MovieModel

class MovieDataSource(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : PagingSource<Int, MovieModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val page = params.key ?: 1
        return try {
            val response = getPopularMoviesUseCase.execute(page)
            val movies = response.movies
            LoadResult.Page(
                data = movies,
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
