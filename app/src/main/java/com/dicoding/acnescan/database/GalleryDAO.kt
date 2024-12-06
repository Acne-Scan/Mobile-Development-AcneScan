package com.dicoding.acnescan.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: GalleryEntity)

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC")
    suspend fun getAllHistory(): List<GalleryEntity>

    // Tambahkan query untuk menghapus seluruh data
    @Query("DELETE FROM history_table")
    suspend fun deleteAllHistory()

    // Menambahkan query untuk menghapus data berdasarkan ID
    @Query("DELETE FROM history_table WHERE id = :id")
    suspend fun deleteHistoryById(id: Int)
}