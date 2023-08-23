package com.example.capstonedesign.response


import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var response: ResponseData
)
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
data class ResponseData(
    @SerializedName("searchItems")
    val searchItems: List<SearchItem>
)

