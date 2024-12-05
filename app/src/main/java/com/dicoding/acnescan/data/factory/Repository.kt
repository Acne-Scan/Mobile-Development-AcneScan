package com.dicoding.acnescan.data.factory

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.acnescan.data.model.response.DataItem
import com.dicoding.acnescan.data.model.retrofit.ApiService
import com.dicoding.acnescan.data.utils.ResultState

class Repository private constructor(private val apiService: ApiService) {

    fun getArticles(): LiveData<ResultState<List<DataItem>>> = liveData {
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

//    fun getProducts(): LiveData<ResultState<Map<String, String>?>> = liveData {
//        emit(ResultState.Loading)
//        try {
//            val response = apiService.uploadImage()
//            val products = response.product_images
//            if (products.isNotEmpty()) {
//                emit(ResultState.Success(products))
//            } else {
//                emit(ResultState.Error("No products available"))
//            }
//        } catch (e: Exception) {
//            emit(ResultState.Error(e.localizedMessage ?: "An unknown error occurred"))
//            }
//        }
//    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(apiService: ApiService): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService).also { instance = it }
            }
    }
}