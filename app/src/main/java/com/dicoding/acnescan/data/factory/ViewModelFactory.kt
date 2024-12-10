package com.dicoding.acnescan.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.acnescan.ui.history.HistoryAnalyzeViewModel
import com.dicoding.acnescan.ui.home.HomeViewModel
import com.dicoding.acnescan.ui.login.LoginViewModel
import com.dicoding.acnescan.ui.profile.ProfileViewModel
import com.dicoding.acnescan.ui.products.ProductsViewModel
import com.dicoding.acnescan.ui.register.RegisterViewModel

// ViewModelFactory untuk membuat ViewModel dengan dependency Injection
class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProductsViewModel::class.java) -> {
                ProductsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryAnalyzeViewModel::class.java) -> {
                HistoryAnalyzeViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    // Fungsi untuk menyediakan instance ViewModelFactory
    companion object {
        fun getInstance(context: Context): ViewModelFactory {
            // Menyuntikkan Repository melalui Injection
            return ViewModelFactory(Injection.provideUserRepository(context))
        }
    }
}