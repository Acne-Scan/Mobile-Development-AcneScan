package com.dicoding.acnescan.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "http://18.141.72.15:8080/" // Ganti sesuai base URL API Anda

    // Fungsi untuk membuat instance Retrofit
    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Konverter JSON ke data class
            .build()

        return retrofit.create(ApiService::class.java)
    }
}