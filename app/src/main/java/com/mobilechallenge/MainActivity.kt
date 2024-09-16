package com.mobilechallenge

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mobilechallenge.databinding.ActivityMainBinding
import com.mobilechallenge.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainFragment.OnToolbarTitleChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as? NavHostFragment
        navController = navHostFragment?.navController ?: throw IllegalStateException("NavHostFragment not found")

        navController.addOnDestinationChangedListener { _, destination, _ ->
            invalidateOptionsMenu()
            updateToolbarTitleVisibility(destination.id)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onToolbarTitleChange(title: String) {
        updateToolbarTitle(title)
    }

    private fun updateToolbarTitle(title: String) {
        binding.toolbarTitle.text = title
    }

    private fun updateToolbarTitleVisibility(fragmentId: Int) {
        if (fragmentId == R.id.detailsFragment) {
            binding.toolbarTitle.visibility = View.GONE
        } else {
            binding.toolbarTitle.visibility = View.VISIBLE
        }
    }
}