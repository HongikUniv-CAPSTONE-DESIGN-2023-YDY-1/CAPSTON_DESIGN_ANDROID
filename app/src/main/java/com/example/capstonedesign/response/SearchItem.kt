package com.example.capstonedesign.response


import com.google.gson.annotations.SerializedName

data class SearchItem(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pricePerGroup")
    val pricePerGroup: Int,
    @SerializedName("pricePerUnit")
    val pricePerUnit: Int,
    @SerializedName("promotion")
    val promotion: String
)