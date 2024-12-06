package com.dicoding.acnescan.data.model.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<DataItemProducts>,

	@field:SerializedName("message")
	val message: String
)

data class DataItemProducts(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("recommendation_id")
	val recommendationId: Int,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String
)
