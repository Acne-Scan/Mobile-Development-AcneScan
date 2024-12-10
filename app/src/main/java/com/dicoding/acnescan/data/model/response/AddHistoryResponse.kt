package com.dicoding.acnescan.data.model.response

import com.google.gson.annotations.SerializedName

data class AddHistoryResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)