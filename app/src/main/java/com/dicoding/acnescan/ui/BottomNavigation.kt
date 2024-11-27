package com.dicoding.acnescan.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.acnescan.R
import com.dicoding.acnescan.databinding.ActivityBottomNavBinding

class BottomNavigation : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = NavHostFragment.findNavController(supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_bottom_nav)!!)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_rekomendasi, R.id.navigation_notifications, R.id.navigation_camera
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}