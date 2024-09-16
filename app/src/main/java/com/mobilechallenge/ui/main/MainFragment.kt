package com.mobilechallenge.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mobilechallenge.R
import com.mobilechallenge.core.network.utils.NetworkUtils
import com.mobilechallenge.databinding.FragmentMainBinding
import com.mobilechallenge.ui.details.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private val sharedViewModel: DetailsViewModel by activityViewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: FragmentMainBinding
    private lateinit var networkUtils: NetworkUtils
    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    private val isNetworkAvailable: LiveData<Boolean> get() = _isNetworkAvailable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkUtils = NetworkUtils(requireContext())

        setupRecyclerView()
        setupSwipeRefresh()

        observeNetworkConnectivity()
        checkNetworkAndObserveMovies()

        isNetworkAvailable.observe(viewLifecycleOwner, Observer { available ->
            if (available) {
                observeMovies()
            } else {
                showNoInternetSnackbar()
            }
        })
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
        _isNetworkAvailable.value = networkAvailable

        if (!networkAvailable) {
            showNoInternetSnackbar()
            binding.swipeRefreshLayout.isRefreshing = false
        } else {
            observeMovies()
        }
    }

    private fun observeMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.movies.collectLatest { pagingData ->
                movieAdapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh !is androidx.paging.LoadState.Loading) {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
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
            if (networkUtils.isNetworkAvailable()) {
                observeMovies()
            } else {
                showNoInternetSnackbar()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun observeNetworkConnectivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                _isNetworkAvailable.value = networkUtils.isNetworkAvailable()
                kotlinx.coroutines.delay(10000)
            }
        }
    }
}