package com.dicoding.acnescan.ui.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.acnescan.database.HistoryEntity
import com.dicoding.acnescan.database.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val historyRepository = HistoryRepository(application)

    private val _history = MutableLiveData<List<HistoryEntity>>()
    val history: LiveData<List<HistoryEntity>> get() = _history

    init {
        fetchHistory()
    }

    // Fungsi untuk mengambil data dari repository
    private fun fetchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = historyRepository.getAllHistory() // Panggil fungsi suspend
                _history.postValue(data) // Update LiveData di background thread
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error fetching history: ${e.message}")
                _errorMessage.postValue("Failed to fetch history: ${e.message}")
            }
        }
    }

    // Fungsi untuk menambahkan data ke repository
    fun insert(history: HistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historyRepository.insert(history) // Panggil fungsi suspend
                Log.d("HistoryViewModel", "Data berhasil ditambahkan ke Room Database")
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error inserting data: ${e.message}")
            }
        }
    }

    // Fungsi untuk menghapus seluruh history
    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historyRepository.deleteAllHistory()
                fetchHistory() // Muat ulang data setelah penghapusan
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error deleting all history: ${e.message}")
            }
        }
    }

    // Fungsi untuk refresh data
    fun refreshHistory() {
        fetchHistory() // Panggil ulang fungsi fetch
    }
}