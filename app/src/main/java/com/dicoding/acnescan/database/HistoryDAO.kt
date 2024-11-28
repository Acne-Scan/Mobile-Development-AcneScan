package com.dicoding.acnescan.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC")
    suspend fun getAllHistory(): List<HistoryEntity>

    // Tambahkan query untuk menghapus seluruh data
    @Query("DELETE FROM history_table")
    suspend fun deleteAllHistory()
}
