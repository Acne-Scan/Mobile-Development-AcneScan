package com.dicoding.acnescan.database

import android.app.Application

class GalleryRepository(application: Application) {
    private val mHistoryDAO: HistoryDao

    init {
        val db = GalleryDatabase.getDatabase(application)
        mHistoryDAO = db.historyDao()
    }

    // Fungsi untuk mengambil semua data history
    suspend fun getAllHistory(): List<GalleryEntity> {
        return mHistoryDAO.getAllHistory()
    }

    // Fungsi untuk menyisipkan data
    suspend fun insert(history: GalleryEntity) {
        mHistoryDAO.insert(history)
    }

    // Fungsi untuk menghapus semua history
    suspend fun deleteAllHistory() {
        mHistoryDAO.deleteAllHistory()
    }

    // Fungsi untuk menghapus history berdasarkan ID
    suspend fun deleteHistoryById(id: Int) {
        mHistoryDAO.deleteHistoryById(id)
    }
}
