package com.mobilechallenge.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobilechallenge.core.model.data.Movie
import com.mobilechallenge.core.model.data.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {

        private val _selectedMovie = MutableLiveData<MovieModel>()
        val selectedMovie: LiveData<MovieModel> get() = _selectedMovie

        fun selectMovie(movie: MovieModel) {
            _selectedMovie.value = movie
        }
}