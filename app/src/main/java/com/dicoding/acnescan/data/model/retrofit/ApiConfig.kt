package com.dicoding.acnescan.data.model.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        val baseURL = "https://acnescan-final.et.r.appspot.com/"

        fun getApiService(): ApiService {

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

object ApiConfigML {
    fun getApiServiceML(): ApiService {
        // Logging Interceptor untuk mencatat request dan response
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Membuat OkHttpClient dengan logging dan timeout settings
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Menambahkan logging interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Waktu untuk membuat koneksi
            .writeTimeout(30, TimeUnit.SECONDS)  // Waktu untuk menulis data
            .readTimeout(30, TimeUnit.SECONDS)   // Waktu untuk membaca respon
            .build()

        // Membuat Retrofit instance dengan OkHttpClient yang telah disesuaikan
        val retrofit = Retrofit.Builder()
            .baseUrl("https://acnescan-1018654955885.asia-southeast1.run.app/") // Base URL untuk API
            .addConverterFactory(GsonConverterFactory.create()) // Untuk konversi JSON ke objek Kotlin
            .client(client) // Menggunakan OkHttpClient yang telah dikonfigurasi
            .build()

        // Mengembalikan API service yang telah disiapkan
        return retrofit.create(ApiService::class.java)
    }
}