package com.dicoding.acnescan.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.acnescan.database.HistoryDao
import com.dicoding.acnescan.database.HistoryDatabase
import com.dicoding.acnescan.database.HistoryEntity
import com.dicoding.acnescan.database.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData yang akan diobservasi oleh Fragment
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //LiveData Database
    private val historyRepository = HistoryRepository(application)

    private val _history = MutableLiveData<List<HistoryEntity>>()
    val history: LiveData<List<HistoryEntity>> get() = _history

    init {
        fetchHistory()
    }

    //Fungsi memanggil events
    private fun fetchHistory() {
        historyRepository.getAllHistory().observeForever { favorites ->
            _history.value = favorites
        }
    }

    fun insert(history: HistoryEntity) = viewModelScope.launch {
        historyRepository.insert(history)
    }
}