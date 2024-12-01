package com.dicoding.acnescan.data.retrofit

import com.dicoding.acnescan.data.response.ArticleResponse
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    suspend fun getAllArticles(): List<ArticleResponse>
}