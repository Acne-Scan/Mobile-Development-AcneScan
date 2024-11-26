package com.dicoding.acnescan.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.acnescan.response.Repository
import com.dicoding.acnescan.retrofit.ApiService
import com.dicoding.acnescan.ui.home.HomeViewModel

class ViewModelFactory (private val Repository : Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(Repository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        fun getInstance (context: Context) : ViewModelFactory {
            return ViewModelFactory(Injection.provideUserRepository(context))
        }
    }
}