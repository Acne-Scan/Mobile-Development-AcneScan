package com.dicoding.acnescan.database

import android.app.Application

class HistoryRepository(application: Application) {
    private val mHistoryDAO: HistoryDao

    init {
        val db = HistoryDatabase.getDatabase(application)
        mHistoryDAO = db.historyDao()
    }

    // Suspend function untuk mengambil semua data history
    suspend fun getAllHistory(): List<HistoryEntity> {
        return mHistoryDAO.getAllHistory()
    }

    // Suspend function untuk menyisipkan data
    suspend fun insert(history: HistoryEntity) {
        mHistoryDAO.insert(history)
    }

    // Suspend function untuk menghapus semua history
    suspend fun deleteAllHistory() {
        mHistoryDAO.deleteAllHistory()
    }
}
