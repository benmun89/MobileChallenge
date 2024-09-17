package com.mobilechallenge.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobilechallenge.core.data.repository.MovieLocalRepository
import com.mobilechallenge.core.model.data.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieLocalRepository: MovieLocalRepository
) : ViewModel() {

    private val _selectedMovie = MutableLiveData<MovieModel>()
    val selectedMovie: LiveData<MovieModel> get() = _selectedMovie

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun selectMovie(movie: MovieModel) {
        _selectedMovie.value = movie
        checkIfFavorite(movie.id)
    }

    private fun checkIfFavorite(movieId: Long) {
        viewModelScope.launch {
            val favorite = movieLocalRepository.isMovieInDatabase(movieId)
            _isFavorite.value = favorite
        }
    }

    fun toggleFavoriteMovie() {
        _selectedMovie.value?.let { movie ->
            viewModelScope.launch {
                if (movieLocalRepository.isMovieInDatabase(movie.id)) {
                    movieLocalRepository.deleteMovie(movie.id)
                } else {
                    movieLocalRepository.insertMovie(movie)
                }
                checkIfFavorite(movie.id)
            }
        }
    }
}
