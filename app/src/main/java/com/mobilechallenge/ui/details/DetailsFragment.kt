package com.mobilechallenge.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
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
            }
        }
    }
}
