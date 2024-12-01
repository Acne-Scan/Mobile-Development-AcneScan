package com.dicoding.acnescan.data.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

    @field:SerializedName("code")
	val code: Int,

    @field:SerializedName("data")
	val data: List<DataItem>,

    @field:SerializedName("message")
	val message: String
)

data class DataItem(

	@field:SerializedName("article_id")
	val articleId: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String
)
