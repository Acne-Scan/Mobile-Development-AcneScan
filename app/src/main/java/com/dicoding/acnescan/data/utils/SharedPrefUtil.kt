package com.dicoding.acnescan.util

import android.content.Context
import android.util.Log

object SharedPrefUtil {

    private const val PREF_NAME = "AppPrefs"

    // Mengambil token dari SharedPreferences
    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    // Mengambil username dari SharedPreferences
    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    // Menghapus semua data pengguna (token dan username)
    fun clear(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        Log.d("ProfileViewModel", "Data pengguna dihapus dan logout berhasil")
    }
}