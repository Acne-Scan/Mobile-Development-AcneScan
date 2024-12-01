package com.dicoding.acnescan.factory

import android.content.Context
import com.dicoding.acnescan.data.response.Repository
import com.dicoding.acnescan.data.retrofit.ApiConfig

object Injection {
    fun provideUserRepository (context: Context) : Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}