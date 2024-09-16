package com.mobilechallenge.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mobilechallenge.R
import com.mobilechallenge.core.network.utils.NetworkUtils
import com.mobilechallenge.databinding.FragmentMainBinding
import com.mobilechallenge.ui.adapter.MovieAdapter
import com.mobilechallenge.ui.adapter.MovieLoadStateAdapter
import com.mobilechallenge.ui.details.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    interface OnToolbarTitleChangeListener {
        fun onToolbarTitleChange(title: String)
    }

    private val movieViewModel: MovieViewModel by viewModels()
    private val sharedViewModel: DetailsViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var networkUtils: NetworkUtils
    private val isNetworkAvailable = MutableLiveData<Boolean>()
    private var titleChangeListener: OnToolbarTitleChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        titleChangeListener = context as? OnToolbarTitleChangeListener
    }

    override fun onDetach() {
        super.onDetach()
        titleChangeListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkUtils = NetworkUtils(requireContext())

        setupRecyclerView()
        setupSwipeRefresh()
        observeNetworkConnectivity()
        checkNetworkAndObserveMovies()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_now_playing -> {
                movieViewModel.setMovieListType(MovieListType.NOW_PLAYING)
                titleChangeListener?.onToolbarTitleChange(getString(R.string.now_playing_toolbar))
                true
            }
            R.id.action_most_popular -> {
                movieViewModel.setMovieListType(MovieListType.POPULAR)
                titleChangeListener?.onToolbarTitleChange(getString(R.string.most_popular_toolbar))
                true
            }
            R.id.action_favorites -> {
                movieViewModel.setMovieListType(MovieListType.FAVORITES)
                titleChangeListener?.onToolbarTitleChange(getString(R.string.favorites_toolbar))
                binding.swipeRefreshLayout.isEnabled = false
                true
            }
            R.id.action_toggle_view -> {
                movieViewModel.toggleViewType()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun observeViewModel() {
        movieViewModel.isGridView.observe(viewLifecycleOwner, Observer { isGridView ->
            updateLayoutManager(isGridView)
        })

        movieViewModel.currentMovieListType.observe(viewLifecycleOwner, Observer { currentListType ->
            when (currentListType) {
                MovieListType.POPULAR -> {
                    binding.swipeRefreshLayout.isEnabled = true
                    movieViewModel.loadPopularMovies()
                    titleChangeListener?.onToolbarTitleChange(getString(R.string.most_popular_toolbar))
                }
                MovieListType.NOW_PLAYING -> {
                    binding.swipeRefreshLayout.isEnabled = true
                    movieViewModel.loadNowPlayingMovies()
                }
                MovieListType.FAVORITES -> {
                    binding.swipeRefreshLayout.isEnabled = false
                    movieViewModel.moviesFromDatabase.observe(viewLifecycleOwner, Observer { pagingData ->
                        movieAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    })
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.currentMovieList.asFlow().collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun updateLayoutManager(isGridView: Boolean) {
        binding.recyclerView.layoutManager = if (isGridView) {
            GridLayoutManager(context, 2)
        } else {
            LinearLayoutManager(context)
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter { movie ->
            sharedViewModel.selectMovie(movie)
            findNavController().navigate(R.id.action_mainFragment_to_detailsFragment)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { movieAdapter.retry() },
                footer = MovieLoadStateAdapter { movieAdapter.retry() }
            )
        }
    }

    private fun checkNetworkAndObserveMovies() {
        val networkAvailable = networkUtils.isNetworkAvailable()
        isNetworkAvailable.value = networkAvailable

        if (networkAvailable) {
            observeViewModel()
        } else {
            showNoInternetSnackbar()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showNoInternetSnackbar() {
        Snackbar.make(requireView(), "No Internet Connection", Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                checkNetworkAndObserveMovies()
            }
            .show()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            when (movieViewModel.getCurrentListType()) {
                MovieListType.FAVORITES -> {
                    binding.swipeRefreshLayout.isEnabled = false
                }
                MovieListType.POPULAR, MovieListType.NOW_PLAYING -> {
                    if (networkUtils.isNetworkAvailable()) {
                        checkNetworkAndObserveMovies()
                    } else {
                        showNoInternetSnackbar()
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun observeNetworkConnectivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                isNetworkAvailable.value = networkUtils.isNetworkAvailable()
                kotlinx.coroutines.delay(10000)
            }
        }
    }
}