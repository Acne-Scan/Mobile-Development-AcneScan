package com.dicoding.acnescan.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository (application: Application) {
    private val mHistoryDAO: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        val db = HistoryDatabase.getDatabase(application)
        mHistoryDAO = db.historyDao()
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> = mHistoryDAO.getAllHistory()

    fun insert (favoriteEvent: HistoryEntity) {
        executorService.execute { mHistoryDAO.insert(favoriteEvent) }
    }

//    fun delete (favoriteEvent: HistoryEntity) {
//        executorService.execute { mHistoryDAO.delete(favoriteEvent) }
//    }
}