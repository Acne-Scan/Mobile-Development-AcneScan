package com.dicoding.acnescan.data.model.response

// Data class untuk format JSON
data class ImageRequest(
    val image: String // Gambar yang dikodekan dalam format base64
)

// Data class untuk product untuk intent
data class Product(
    val name: String,
    val imageUrl: String,
    val productUrl: String
)

data class HistoryItem(
    val title: String,
    val description: String
)