package com.dicoding.acnescan.data.retrofit

import com.dicoding.acnescan.data.response.FileUploadResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Data class untuk format JSON
data class ImageRequest(
    val image: String // Gambar yang dikodekan dalam format base64
)

interface ApiServiceML {
    @POST("/predict")
    suspend fun uploadImage(
        @Body imageRequest: ImageRequest // Mengirimkan request body yang berisi image dalam base64
    ): Response<FileUploadResponse>
}