package com.dicoding.acnescan.factory

import android.content.Context
import com.dicoding.acnescan.response.Repository
import com.dicoding.acnescan.retrofit.ApiConfig

object Injection {
    fun provideUserRepository (context: Context) : Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}