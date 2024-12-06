package com.dicoding.acnescan.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.acnescan.R
import com.dicoding.acnescan.data.adapter.FragmentAdapter
import com.dicoding.acnescan.databinding.ActivityBottomNavBinding
import com.dicoding.acnescan.ui.camera.CameraActivity
import com.dicoding.acnescan.ui.history.HistoryAnalyzeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigation : AppCompatActivity() {

    internal lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView
        val viewPager: ViewPager2 = binding.navHostFragmentActivityBottomNav

        // Setup Adapter untuk ViewPager2
        val adapter = FragmentAdapter(this)  // Pastikan menggunakan AppCompatActivity sebagai context
        viewPager.adapter = adapter

        // Sinkronkan bottom navigation dengan ViewPager2
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.currentItem = 0
                R.id.navigation_products -> viewPager.currentItem = 1
                R.id.navigation_local_gallery -> viewPager.currentItem = 2
            }
            true
        }

        // Menambahkan listener untuk FAB
        val fabCamera = findViewById<AppCompatImageButton>(R.id.fab_camera)
        fabCamera.setOnClickListener {
            // Ganti dengan CameraActivity ketika FAB diklik
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        // Sinkronisasi ViewPager dengan BottomNavigationView dan memperbarui toolbar title
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        navView.selectedItemId = R.id.navigation_home
                        supportActionBar?.title = getString(R.string.title_home)
                    }
                    1 -> {
                        navView.selectedItemId = R.id.navigation_products
                        supportActionBar?.title = getString(R.string.title_products)
                    }
                    2 -> {
                        navView.selectedItemId = R.id.navigation_local_gallery
                        supportActionBar?.title = getString(R.string.title_gallery)
                    }
                }
            }
        })

        // Set judul default saat pertama kali membuka aplikasi
        supportActionBar?.title = getString(R.string.title_home)
    }

    // Menyusun menu di toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    // Menangani menu item yang dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_menu -> {
                val intent = Intent(this, HistoryAnalyzeActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
