package com.dicoding.acnescan.data.factory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.acnescan.data.model.response.DataItemArticles
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.data.model.retrofit.ApiService
import com.dicoding.acnescan.data.utils.ResultState

class Repository private constructor(private val apiService: ApiService) {

    fun getArticles(): LiveData<ResultState<List<DataItemArticles>>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getAllArticles()
            val articles = response.data
            if (articles.isNotEmpty()) {
                emit(ResultState.Success(articles))
            } else {
                emit(ResultState.Error("No articles available"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }

    fun getProducts(): LiveData<ResultState<List<DataItemProducts>>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getAllProducts()
            val products = response.data
            if (products.isNotEmpty()) {
                emit(ResultState.Success(products))
            } else {
                emit(ResultState.Error("No products available"))
            }
        } catch (e: Exception) {
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