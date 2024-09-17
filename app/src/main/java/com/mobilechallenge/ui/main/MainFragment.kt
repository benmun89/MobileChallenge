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
import androidx.recyclerview.widget.RecyclerView
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
        movieViewModel.isGridView.removeObservers(viewLifecycleOwner)
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

    private val menuActions = mapOf(
        R.id.action_now_playing to MovieListType.NOW_PLAYING,
        R.id.action_most_popular to MovieListType.POPULAR,
        R.id.action_favorites to MovieListType.FAVORITES,
        R.id.action_toggle_view to null,
        R.id.action_filter_date to MovieListType.FILTER_BY_NAME
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val listType = menuActions[item.itemId]
        if (listType != null) {
            movieViewModel.setMovieListType(listType)
            titleChangeListener?.onToolbarTitleChange(getString(listType.toolbarTitle))
            if (listType != MovieListType.FAVORITES && listType != MovieListType.FILTER_BY_NAME) {
                binding.swipeRefreshLayout.isEnabled = true

            }
            return true
        } else if (item.itemId == R.id.action_toggle_view) {
            movieViewModel.toggleViewType()
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    private fun observeViewModel() {
        titleChangeListener?.onToolbarTitleChange(getString(R.string.most_popular_toolbar))
        if (!movieViewModel.isGridView.hasObservers()) {
            movieViewModel.isGridView.observe(viewLifecycleOwner, Observer { isGridView ->
                updateLayoutManager(isGridView)
            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.currentMovieList.asFlow().collectLatest { pagingData ->

                movieAdapter.submitData(pagingData)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        movieViewModel.currentMovieListType.observe(viewLifecycleOwner) { currentListType ->
            if (currentListType == MovieListType.FAVORITES) {
                movieViewModel.moviesFromDatabase.observe(viewLifecycleOwner) { pagingData ->
                    movieAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    binding.swipeRefreshLayout.isRefreshing = false

                }
            }
        }
    }

    private fun updateLayoutManager(isGridView: Boolean) {
        val layoutManager = if (isGridView) {
            GridLayoutManager(requireContext(), 2)
        } else {
            LinearLayoutManager(requireContext())
        }
        binding.recyclerView.layoutManager = layoutManager
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
        movieAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
            }
        })
    }

    private fun checkNetworkAndObserveMovies() {
        val networkAvailable = networkUtils.isNetworkAvailable()
        isNetworkAvailable.value = networkAvailable

        if (networkAvailable) {
            observeViewModel()
        } else {
            showNoInternetSnackBar()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showNoInternetSnackBar() {
        Snackbar.make(requireView(),
            getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.retry)) {
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
                        showNoInternetSnackBar()
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }

                MovieListType.FILTER_BY_NAME -> {
                    showNoInternetSnackBar()
                    binding.swipeRefreshLayout.isRefreshing = false
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