package com.dicoding.acnescan.data.factory

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.acnescan.data.model.response.DataItemArticles
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.data.model.response.LoginRequest
import com.dicoding.acnescan.data.model.response.LoginResponse
import com.dicoding.acnescan.data.model.response.RegisterRequest
import com.dicoding.acnescan.data.model.response.RegisterResponse
import com.dicoding.acnescan.data.model.retrofit.ApiService
import com.dicoding.acnescan.data.utils.ResultState
import com.dicoding.acnescan.util.SharedPrefUtil
import retrofit2.Response

class Repository private constructor(private val apiService: ApiService) {

    // Fungsi untuk mengambil username dari SharedPreferences
    fun getUsername(context: Context): String? {
        return SharedPrefUtil.getUsername(context)
    }

    // Fungsi untuk mengambil artikel
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

    // Fungsi untuk mengambil produk
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

    // Fungsi untuk melakukan login
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }

    // Fungsi untuk melakukan registrasi
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return apiService.register(registerRequest)
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