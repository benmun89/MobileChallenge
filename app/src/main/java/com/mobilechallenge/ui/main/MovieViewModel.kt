package com.mobilechallenge.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.mobilechallenge.core.data.repository.MovieLocalRepository
import com.mobilechallenge.core.domain.GetNowPlayingMoviesUseCase
import com.mobilechallenge.core.domain.GetPopularMoviesUseCase
import com.mobilechallenge.core.model.data.MovieModel
import com.mobilechallenge.core.model.data.mapper.toDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val movieLocalRepository: MovieLocalRepository
) : ViewModel() {

    private val _currentMovieListType = MutableLiveData<MovieListType>(MovieListType.POPULAR) // Default to Popular
    val currentMovieListType: LiveData<MovieListType> get() = _currentMovieListType

    private var currentListType: MovieListType = MovieListType.POPULAR
    private val _moviesFromDatabase = MutableLiveData<PagingData<MovieModel>>()
    val moviesFromDatabase: LiveData<PagingData<MovieModel>> get() = _moviesFromDatabase

    private val _currentMovieList = MutableLiveData<PagingData<MovieModel>>()
    val currentMovieList: LiveData<PagingData<MovieModel>> get() = _currentMovieList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _isGridView = MutableLiveData<Boolean>(true)
    val isGridView: LiveData<Boolean> get() = _isGridView

    fun loadFavoriteMovies() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { movieLocalRepository.getAllMovies() }
            ).flow
                .map { pagingData -> pagingData.map { it.toDomainModel() } }
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _moviesFromDatabase.postValue(pagingData)
                }
        }
    }

    init {
        loadMoviesForCurrentType()
    }

    fun loadPopularMovies() {
        viewModelScope.launch {
            createPopularMoviesFlow().collect {
                _currentMovieList.postValue(it)
            }
        }
    }

    private fun loadMoviesForCurrentType() {
        when (_currentMovieListType.value) {
            MovieListType.POPULAR -> loadPopularMovies()
            MovieListType.NOW_PLAYING -> loadNowPlayingMovies()
            MovieListType.FAVORITES -> loadFavoriteMovies()
            null -> TODO()
        }
    }

    fun loadNowPlayingMovies() {
        viewModelScope.launch {
            createNowPlayingMoviesFlow().collect {
                _currentMovieList.postValue(it)
            }
        }
    }

    private fun createPopularMoviesFlow(): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MovieDataSource(getPopularMoviesUseCase) }
        ).flow.cachedIn(viewModelScope)
    }

    private fun createNowPlayingMoviesFlow(): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NowPlayingMoviePagingSource(getNowPlayingMoviesUseCase) }
        ).flow.cachedIn(viewModelScope)
    }

    fun setMovieListType(type: MovieListType) {
        _currentMovieListType.value = type
        when (type) {
            MovieListType.POPULAR -> loadPopularMovies()
            MovieListType.NOW_PLAYING -> loadNowPlayingMovies()
            MovieListType.FAVORITES -> loadFavoriteMovies()
        }
    }

    fun getCurrentListType(): MovieListType {
        return currentListType
    }

    fun toggleViewType() {
        _isGridView.value = _isGridView.value?.not()
    }
}

enum class MovieListType {
    POPULAR,
    NOW_PLAYING,
    FAVORITES
}