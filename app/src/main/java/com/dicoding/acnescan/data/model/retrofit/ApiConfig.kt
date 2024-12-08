package com.dicoding.acnescan.data.model.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {

    companion object {
        private const val BASE_URL = "https://acnescan-final.et.r.appspot.com/"

        fun getApiService(context: Context): ApiService {
            // Auth Interceptor untuk menambahkan Authorization header
            val authInterceptor = Interceptor { chain ->
                val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token", null)

                val requestBuilder = chain.request().newBuilder()
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)  // Menambahkan authInterceptor
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
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
            .baseUrl("https://acne-scan-v2-1018654955885.us-central1.run.app") // Base URL untuk API
            .addConverterFactory(GsonConverterFactory.create()) // Untuk konversi JSON ke objek Kotlin
            .client(client) // Menggunakan OkHttpClient yang telah dikonfigurasi
            .build()

        // Mengembalikan API service yang telah disiapkan
        return retrofit.create(ApiService::class.java)
    }
}