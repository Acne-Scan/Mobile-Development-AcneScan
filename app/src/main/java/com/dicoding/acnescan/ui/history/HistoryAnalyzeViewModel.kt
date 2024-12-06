package com.dicoding.acnescan.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.acnescan.data.model.response.HistoryItem

class HistoryAnalyzeViewModel : ViewModel() {

    private val _historyItems = MutableLiveData<List<HistoryItem>>()
    val historyItems: LiveData<List<HistoryItem>> = _historyItems

    // Fungsi untuk mendapatkan data dummy
    fun loadHistoryData() {
        // Contoh data dummy
        val items = listOf(
            HistoryItem("History 1", "Description for history 1"),
            HistoryItem("History 2", "Description for history 2"),
            HistoryItem("History 3", "Description for history 3")
        )
        _historyItems.postValue(items)
    }
}
