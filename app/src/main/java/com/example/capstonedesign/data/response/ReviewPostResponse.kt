package com.example.capstonedesign.data.response


import com.google.gson.annotations.SerializedName

data class ReviewPostResponse(
    @SerializedName("data")
    val data: ReviewPostData,
    @SerializedName("message")
    val message: String,
    @SerializedName("errorMessage")
    val errorMessage: String?
)

data class ReviewPostBody(
    val promotionId: Int,
    val star: Int,
    val content: String,
)

data class UpdateReviewBody(
    val commentId: Int,
    val promotionId: Int,
    val star: Int,
    val content: String,
)

data class ReviewPostData(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("promotionId")
    val promotionId: Int,
    @SerializedName("star")
    val star: Int
)
