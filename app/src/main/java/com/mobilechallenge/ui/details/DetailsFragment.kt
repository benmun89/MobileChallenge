package com.mobilechallenge.ui.details

import android.os.Bundle
import android.view.*
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.mobilechallenge.R
import com.mobilechallenge.core.model.data.MovieModel
import com.mobilechallenge.core.network.BuildConfig
import com.mobilechallenge.databinding.DetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

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
            movie?.let {
                bindMovieData(movie)
            }
        }

        detailsViewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            updateFavoriteButton(isFavorite)
        }

        binding.favoriteToggleButton.setOnClickListener {
            detailsViewModel.toggleFavoriteMovie()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        menu.forEach { item ->
            item.isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun bindMovieData(movie: MovieModel) {
        binding.movieTitle.text = movie.title
        binding.movieDescription.text = movie.overview
        binding.movieGenres.text = getString(R.string.genres_format, movie.genreIds.joinToString(", "))
        binding.moviePopularity.text = getString(R.string.popularity_format, movie.popularity.toString())
        binding.movieReleaseDate.text = getString(R.string.release_date_format, movie.releaseDate)
        binding.movieLanguages.text = getString(R.string.languages_format, movie.originalLanguage)
        binding.movieVoteAverage.text = getString(R.string.vote_average_format, movie.voteAverage.toString())

        Glide.with(this)
            .load("${BuildConfig.TMDB_IMAGE_ORIGINAL_URL}${movie.posterPath}")
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.moviePoster)

        Glide.with(this)
            .load("${BuildConfig.TMDB_IMAGE_ORIGINAL_URL}${movie.backdropPath}")
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.backgroundImage)
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        binding.favoriteToggleButton.text = if (isFavorite) "Remove from Favorites" else "Add to Favorites"
    }
}
