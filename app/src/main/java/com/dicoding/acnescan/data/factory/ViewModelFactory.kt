package com.dicoding.acnescan.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.acnescan.ui.home.HomeViewModel
import com.dicoding.acnescan.ui.products.ProductsViewModel

class ViewModelFactory (private val Repository : Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(Repository) as T
            }
            modelClass.isAssignableFrom(ProductsViewModel::class.java) -> {
                ProductsViewModel(Repository) as T
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