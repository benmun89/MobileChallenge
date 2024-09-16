package com.mobilechallenge.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mobilechallenge.R
import com.mobilechallenge.core.network.BuildConfig
import com.mobilechallenge.databinding.DetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private lateinit var binding: DetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel.selectedMovie.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {

                binding.movieTitle.text = movie.title
                binding.movieDescription.text = movie.overview
                binding.movieGenres.text = "Genres: ${movie.genreIds.joinToString(", ")}"
                binding.moviePopularity.text = "Popularity: ${movie.popularity}"
                binding.movieReleaseDate.text = "Release Date: ${movie.releaseDate}"
                binding.movieLanguages.text = "Languages: ${movie.originalLanguage}"
                binding.movieVoteAverage.text = "Vote Average: ${movie.voteAverage}"


                Glide.with(this)
                    .load("${BuildConfig.TMDB_IMAGE_ORIGINAL_URL}${movie.posterPath}")
                    .into(binding.moviePoster)

                Glide.with(this)
                    .load("${BuildConfig.TMDB_IMAGE_ORIGINAL_URL}${movie.backdropPath}")
                    .into(binding.backgroundImage)

                binding.addToFavoritesButton.setOnClickListener {
                    movie.let { selectedMovie ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            try {
                                detailsViewModel.insertMovie(selectedMovie)
                                Snackbar.make(
                                    binding.root,
                                    "Movie added to favorites!",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                Snackbar.make(
                                    binding.root,
                                    "Failed to add movie to favorites.",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        menu.forEach { item ->
            item.isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}