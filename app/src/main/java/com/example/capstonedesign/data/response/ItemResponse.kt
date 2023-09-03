package com.example.capstonedesign.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class ItemResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var response: ResponseData
)
@Parcelize
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
 ): Parcelable
data class ResponseData(
    @SerializedName("searchItems")
    val searchItems: List<SearchItem>
)

