package com.dicoding.acnescan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.acnescan.response.ArticleResponse
import com.dicoding.acnescan.response.Repository
import com.dicoding.acnescan.response.ResultState

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _articles = MutableLiveData<ResultState<List<ArticleResponse>>>() // Menggunakan daftar Artikel
    val articles: LiveData<ResultState<List<ArticleResponse>>> get() = _articles

    fun getArticles() {
        repository.getArticles().observeForever { result ->
            _articles.value = result
        }
    }
}