package com.dicoding.acnescan.data.model.retrofit

import com.dicoding.acnescan.data.model.response.ArticleResponse
import com.dicoding.acnescan.data.model.response.FileUploadResponse
import com.dicoding.acnescan.data.model.response.ImageRequest
import com.dicoding.acnescan.data.model.response.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Service Artikel
    @GET("articles")
    suspend fun getAllArticles(): ArticleResponse

    // Service Produk
    @GET("recommendations")
    suspend fun getAllProducts(): ProductResponse

    // Service Machine Learning
    @POST("/predict")
    suspend fun uploadImage(
        @Body imageRequest: ImageRequest // Mengirimkan request body yang berisi image dalam base64
    ): Response<FileUploadResponse>
}