package com.dicoding.acnescan.ui.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.acnescan.database.GalleryEntity
import com.dicoding.acnescan.database.GalleryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val galleryRepository = GalleryRepository(application)

    private val _gallery = MutableLiveData<List<GalleryEntity>>()
    val gallery: LiveData<List<GalleryEntity>> get() = _gallery

    init {
        fetchHistory()
    }

    // Fungsi untuk mengambil data dari repository
    private fun fetchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = galleryRepository.getAllHistory() // Panggil fungsi suspend
                _gallery.postValue(data) // Update LiveData di background thread
            } catch (e: Exception) {
                Log.e("GalleryViewModel", "Error fetching history: ${e.message}")
                _errorMessage.postValue("Failed to fetch history: ${e.message}")
            }
        }
    }

    // Fungsi untuk menghapus history berdasarkan ID
    fun deleteHistoryById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                galleryRepository.deleteHistoryById(id) // Panggil fungsi suspend untuk menghapus berdasarkan ID
                fetchHistory() // Muat ulang data setelah penghapusan
            } catch (e: Exception) {
                Log.e("GalleryViewModel", "Error deleting history by ID: ${e.message}")
            }
        }
    }

    // Fungsi untuk menambahkan data ke repository
    fun insert(history: GalleryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                galleryRepository.insert(history) // Panggil fungsi suspend
                Log.d("GalleryViewModel", "Data berhasil ditambahkan ke Room Database")
            } catch (e: Exception) {
                Log.e("GalleryViewModel", "Error inserting data: ${e.message}")
            }
        }
    }

    // Fungsi untuk menghapus semua history
    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                galleryRepository.deleteAllHistory()
                fetchHistory() // Muat ulang data setelah penghapusan
            } catch (e: Exception) {
                Log.e("GalleryViewModel", "Error deleting all history: ${e.message}")
            }
        }
    }

    // Fungsi untuk refresh data
    fun refreshHistory() {
        fetchHistory() // Panggil ulang fungsi fetch
    }
}