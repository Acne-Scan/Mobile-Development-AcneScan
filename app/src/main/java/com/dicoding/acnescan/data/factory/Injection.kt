package com.dicoding.acnescan.data.factory

import android.content.Context
import com.dicoding.acnescan.data.model.retrofit.ApiConfig

object Injection {
    fun provideUserRepository (context: Context) : Repository {
        val apiService = ApiConfig.getApiService(context)
        return Repository.getInstance(apiService)
    }
}