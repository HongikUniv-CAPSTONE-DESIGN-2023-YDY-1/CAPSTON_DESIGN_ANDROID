package com.example.capstonedesign.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RecommendListResponse(
    @SerializedName("data")
    val data: List<RecommendData>,
    @SerializedName("message")
    val message: String
)

@Parcelize
data class RecommendData(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("id")
    val id: Int,
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