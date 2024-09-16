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

        fun selectMovie(movie: MovieModel) {
            _selectedMovie.value = movie
        }

    fun insertMovie(movie: MovieModel) {
        viewModelScope.launch {
            movieLocalRepository.insertMovie(movie)
        }
    }
}