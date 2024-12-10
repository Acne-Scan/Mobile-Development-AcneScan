package com.dicoding.acnescan.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.acnescan.data.factory.Repository
import com.dicoding.acnescan.data.model.response.DataItem
import kotlinx.coroutines.launch

class HistoryAnalyzeViewModel(private val repository: Repository) : ViewModel() {

    private val _historyList = MutableLiveData<List<DataItem?>?>()
    val historyList: LiveData<List<DataItem?>?> = _historyList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchHistory() {
        viewModelScope.launch {
            try {
                val response = repository.getHistory()
                if (response.isSuccessful) {
                    _historyList.postValue(response.body()?.data)
                    Log.d("HistoryAnalyzeViewModel", "History fetched successfully")
                } else {
                    _errorMessage.postValue("Error: ${response.code()}")
                    Log.e("HistoryAnalyzeViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Network error: ${e.localizedMessage}")
                Log.e("HistoryAnalyzeViewModel", "Network error: ${e.localizedMessage}")
            }
        }
    }
}