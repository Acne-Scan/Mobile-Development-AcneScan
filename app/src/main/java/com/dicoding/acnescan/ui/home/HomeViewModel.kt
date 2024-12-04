package com.dicoding.acnescan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.acnescan.data.model.response.DataItem
import com.dicoding.acnescan.data.model.response.Repository
import com.dicoding.acnescan.data.utils.ResultState

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _articles = MutableLiveData<ResultState<List<DataItem>>>() // Menggunakan daftar Artikel
    val articles: LiveData<ResultState<List<DataItem>>> get() = _articles

    fun getArticles() {
        repository.getArticles().observeForever { result ->
            _articles.value = result
        }
    }
}