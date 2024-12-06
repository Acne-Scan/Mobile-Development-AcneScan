package com.dicoding.acnescan.ui.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.acnescan.databinding.ActivityHistoryAnalyzeBinding

class HistoryAnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryAnalyzeBinding
    private val viewModel: HistoryAnalyzeViewModel by viewModels()
    private lateinit var adapter: HistoryAnalyzeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        adapter = HistoryAnalyzeAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Mengamati data dari ViewModel
        viewModel.historyItems.observe(this) {
            adapter = HistoryAnalyzeAdapter(it)
            binding.recyclerView.adapter = adapter
        }

        // Memuat data dari ViewModel
        viewModel.loadHistoryData()
    }
}
