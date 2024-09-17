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
import com.mobilechallenge.R
import com.mobilechallenge.core.data.repository.MovieLocalRepository
import com.mobilechallenge.core.domain.GetNowPlayingMoviesUseCase
import com.mobilechallenge.core.domain.GetPopularMoviesUseCase
import com.mobilechallenge.core.model.data.MovieModel
import com.mobilechallenge.core.model.data.mapper.toDomainModel
import com.mobilechallenge.ui.pagingSource.MovieDataSource
import com.mobilechallenge.ui.pagingSource.NowPlayingMoviePagingSource
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

    private val _currentMovieListType = MutableLiveData<MovieListType>(MovieListType.POPULAR)
    val currentMovieListType: LiveData<MovieListType> get() = _currentMovieListType

    private var currentListType: MovieListType = MovieListType.POPULAR
    private val _moviesFromDatabase = MutableLiveData<PagingData<MovieModel>>()
    val moviesFromDatabase: LiveData<PagingData<MovieModel>> get() = _moviesFromDatabase

    private val _currentMovieList = MutableLiveData<PagingData<MovieModel>>()
    val currentMovieList: LiveData<PagingData<MovieModel>> get() = _currentMovieList

    private val _isGridView = MutableLiveData<Boolean>(true)
    val isGridView: LiveData<Boolean> get() = _isGridView

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

    fun loadNowPlayingMovies() {
        viewModelScope.launch {
            createNowPlayingMoviesFlow().collect {
                _currentMovieList.postValue(it)
            }
        }
    }

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

    fun filterFavoriteMoviesByName() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { movieLocalRepository.getAllMoviesOrderByName() }
            ).flow
                .map { pagingData -> pagingData.map { it.toDomainModel() } }
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _moviesFromDatabase.postValue(pagingData)
                }
        }
    }

    private fun loadMoviesForCurrentType() {
        when (_currentMovieListType.value) {
            MovieListType.POPULAR -> loadPopularMovies()
            MovieListType.NOW_PLAYING -> loadNowPlayingMovies()
            MovieListType.FAVORITES -> loadFavoriteMovies()
            MovieListType.FILTER_BY_NAME -> filterFavoriteMoviesByName()
            null -> TODO()
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
            MovieListType.FILTER_BY_NAME -> filterFavoriteMoviesByName()
        }
    }

    fun getCurrentListType(): MovieListType {
        return currentListType
    }

    fun toggleViewType() {
        _isGridView.value = _isGridView.value?.not()
    }
}

enum class MovieListType(val toolbarTitle: Int) {
    POPULAR(R.string.most_popular_toolbar),
    NOW_PLAYING(R.string.now_playing_toolbar),
    FAVORITES(R.string.favorites_toolbar),
    FILTER_BY_NAME(R.string.filter_by_name_toolbar)
}