package com.dicoding.acnescan.data.model.retrofit

import com.dicoding.acnescan.data.model.response.ArticleResponse
import com.dicoding.acnescan.data.model.response.FileUploadResponse
import com.dicoding.acnescan.data.model.response.ImageRequest
import com.dicoding.acnescan.data.model.response.LoginRequest
import com.dicoding.acnescan.data.model.response.LoginResponse
import com.dicoding.acnescan.data.model.response.ProductResponse
import com.dicoding.acnescan.data.model.response.RegisterRequest
import com.dicoding.acnescan.data.model.response.RegisterResponse
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

    // Service authentication
    @POST("auth")
    suspend fun login(
        @Body loginRequest: LoginRequest // Mengirimkan request body yang berisi username dan password
    ): Response<LoginResponse>

    // Service Register
    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest // Mengirimkan request body yang berisi username dan password
    ): Response<RegisterResponse>

    // Service Machine Learning
    @POST("/predict")
    suspend fun uploadImage(
        @Body imageRequest: ImageRequest // Mengirimkan request body yang berisi image dalam base64
    ): Response<FileUploadResponse>
}