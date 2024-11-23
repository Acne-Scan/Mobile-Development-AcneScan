package com.dicoding.acnescan.retrofit

import com.dicoding.acnescan.response.ArticleResponse
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    suspend fun getAllArticles(): ArticleResponse
}