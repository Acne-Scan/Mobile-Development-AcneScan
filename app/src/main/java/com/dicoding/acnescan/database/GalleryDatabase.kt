package com.dicoding.acnescan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GalleryEntity::class], version = 1, exportSchema = false)
abstract class GalleryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: GalleryDatabase? = null

        fun getDatabase(context: Context): GalleryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GalleryDatabase::class.java,
                    "history_database"
                )
//                    .fallbackToDestructiveMigration() // Menambahkan opsi ini
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}