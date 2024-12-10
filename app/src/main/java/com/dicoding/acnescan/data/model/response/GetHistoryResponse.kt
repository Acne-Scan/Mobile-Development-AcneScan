package com.dicoding.acnescan.data.model.response

import com.google.gson.annotations.SerializedName

data class GetHistoryResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("image")
	val productImage: Map<String, String>,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("prediction")
	val prediction: String? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("history_id")
	val historyId: Int? = null,

	@field:SerializedName("product_links")
	val productLinks: Map<String, String>,

	@field:SerializedName("user_picture")
	val userPicture: String? = null,
)