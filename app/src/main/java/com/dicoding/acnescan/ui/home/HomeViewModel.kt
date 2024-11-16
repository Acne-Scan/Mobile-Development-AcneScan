package com.dicoding.acnescan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.acnescan.R
import com.dicoding.acnescan.model.ArticleItem
import com.dicoding.acnescan.model.RecommendationItem

class HomeViewModel : ViewModel() {
    private val _recommendations = MutableLiveData<List<RecommendationItem>>().apply {
        value = listOf(
            RecommendationItem(R.drawable.azarine_sunscreen, "Azarine Sunscreen Gel"),
            RecommendationItem(R.drawable.pp_patrickk, "Another Facial Wash")
        )
    }
    val recommendations: LiveData<List<RecommendationItem>> = _recommendations

    private val _articles = MutableLiveData<List<ArticleItem>>().apply {
        value = listOf(
            ArticleItem(R.drawable.makeup_tips, "Makeup Tips 2023"),
            ArticleItem(R.drawable.pp_patrickk, "12 Cara Hilangkan Jerawat")
        )
    }
    val articles: LiveData<List<ArticleItem>> = _articles

    val greeting: LiveData<String> = MutableLiveData("Hello, User!")
}