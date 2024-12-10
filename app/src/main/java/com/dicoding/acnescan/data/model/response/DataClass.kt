package com.dicoding.acnescan.data.model.response

// Data class untuk format JSON
data class ImageRequest(
    val image: String // Gambar yang dikodekan dalam format base64
)

// Data class untuk request login
data class LoginRequest(
    val username: String,
    val password: String
)

// Data class untuk product untuk intent
data class Product(
    val name: String,
    val imageUrl: String,
    val productUrl: String
)

// Data class untuk history sudah dianalysis
data class AddHistoryRequest(
    val image: Map<String, String>,
    val prediction: String,
    val recommendation: String,
    val product_links: Map<String, String>,
    val user_picture: String
)

// Data class untuk request register
data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String = "user" // Role sudah tetap 'user'
)