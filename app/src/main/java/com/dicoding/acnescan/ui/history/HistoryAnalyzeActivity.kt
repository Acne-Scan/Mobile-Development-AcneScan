package com.dicoding.acnescan.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.acnescan.data.factory.ViewModelFactory
import com.dicoding.acnescan.databinding.ActivityHistoryAnalyzeBinding
import com.dicoding.acnescan.util.SharedPrefUtil

class HistoryAnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryAnalyzeBinding
    private lateinit var historyAnalyzeAdapter: HistoryAnalyzeAdapter

    // Menggunakan ViewModelFactory untuk mendapatkan instance dari HistoryAnalyzeViewModel
    private val historyAnalyzeViewModel: HistoryAnalyzeViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)  // Menggunakan ViewModelFactory untuk mendapatkan ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        historyAnalyzeAdapter = HistoryAnalyzeAdapter(emptyList()) // Inisialisasi dengan list kosong
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = historyAnalyzeAdapter

        // Observasi data history
        historyAnalyzeViewModel.historyList.observe(this) { historyItems ->
            binding.progressBar.visibility = View.GONE  // Sembunyikan ProgressBar setelah data selesai dimuat
            if (historyItems != null) {
                if (historyItems.isEmpty()) {
                    binding.tvNoHistory.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.tvNoHistory.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    historyAnalyzeAdapter.updateData(historyItems)  // Update adapter dengan data baru
                }
            }
        }

        // Observasi error message jika ada
        historyAnalyzeViewModel.errorMessage.observe(this) { errorMessage ->
            binding.progressBar.visibility = View.GONE  // Sembunyikan ProgressBar jika ada error
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Ambil token dari SharedPreferences
        val token = SharedPrefUtil.getToken(this)
        if (token != null) {
            // Jika token valid, panggil fetchHistory
            binding.progressBar.visibility = View.VISIBLE  // Tampilkan ProgressBar sebelum memulai load
            historyAnalyzeViewModel.fetchHistory()
        } else {
            // Jika token tidak ditemukan
            Log.e("HistoryAnalyzeActivity", "Token tidak ditemukan")
        }

        // Tombol kembali untuk kembali ke activity sebelumnya
        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}