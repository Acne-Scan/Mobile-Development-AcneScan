package com.dicoding.acnescan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.acnescan.data.factory.Repository
import com.dicoding.acnescan.data.model.response.DataItemArticles
import com.dicoding.acnescan.data.model.response.DataItemProducts
import com.dicoding.acnescan.data.utils.ResultState

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _articles = MutableLiveData<ResultState<List<DataItemArticles>>>() // Menggunakan daftar Artikel
    private val _products = MutableLiveData<ResultState<List<DataItemProducts>>>() // Menggunakan daftar Produk

    val articles: LiveData<ResultState<List<DataItemArticles>>> get() = _articles
    val products: LiveData<ResultState<List<DataItemProducts>>> get() = _products

    fun getArticles() {
        repository.getArticles().observeForever { result ->
            _articles.value = result
        }
    }

    fun getProducts() {
        repository.getProducts().observeForever { result ->
            _products.value = result
        }
    }
}