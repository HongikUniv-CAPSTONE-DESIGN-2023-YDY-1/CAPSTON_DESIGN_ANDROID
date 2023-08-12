package com.example.capstonedesign.response


import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: Response
)