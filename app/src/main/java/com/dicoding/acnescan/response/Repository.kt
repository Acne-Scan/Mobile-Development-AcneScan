package com.dicoding.acnescan.response

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.acnescan.retrofit.ApiService

class Repository private constructor(private val apiService: ApiService) {

    fun getArticles(): LiveData<ResultState<List<ArticleResponse>>> = liveData {
        emit(ResultState.Loading) // Emit status loading
        try {
            val response = apiService.getAllArticles() // Panggil API
            if (response.isNotEmpty()) {
                emit(ResultState.Success(response)) // Emit hasil jika sukses
            } else {
                emit(ResultState.Error("No articles available")) // Emit error jika kosong
            }
        } catch (e: Exception) {
            // Emit error dengan pesan yang lebih aman
            emit(ResultState.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(apiService: ApiService): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService).also { instance = it }
            }
    }
}
