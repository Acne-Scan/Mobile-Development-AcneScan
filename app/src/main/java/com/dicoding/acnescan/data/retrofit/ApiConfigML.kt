package com.dicoding.acnescan.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfigML {
    fun getApiService(): ApiServiceML {
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
        return retrofit.create(ApiServiceML::class.java)
    }
}