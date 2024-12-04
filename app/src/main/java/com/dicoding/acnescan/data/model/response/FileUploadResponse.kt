package com.dicoding.acnescan.data.model.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
	@field:SerializedName("success")
	val success: Boolean? = null, // Menambahkan status sukses

	@field:SerializedName("prediction")
	val prediction: String? = null, // Klasifikasi atau label yang diprediksi

	@field:SerializedName("confidence")
	val confidence: Float? = null, // Nilai kepercayaan prediksi, lebih baik tipe float daripada String

	@field:SerializedName("recommendation")
	val recommendation: String? = null, // Rekomendasi berdasarkan prediksi

	@field:SerializedName("product_images")
	val productImages: Map<String, String>? = null, // Gambar produk yang terkait dengan prediksi

	@field:SerializedName("all_predictions")
	val allPredictions: List<PredictionItem>? = null // Daftar prediksi lain yang dilakukan oleh model
)

data class PredictionItem(
	@field:SerializedName("class")
	val predictionClass: String? = null, // Kelas prediksi

	@field:SerializedName("confidence")
	val confidence: Float? = null // Kepercayaan untuk setiap prediksi
)

// Data class untuk format JSON
data class ImageRequest(
	val image: String // Gambar yang dikodekan dalam format base64
)