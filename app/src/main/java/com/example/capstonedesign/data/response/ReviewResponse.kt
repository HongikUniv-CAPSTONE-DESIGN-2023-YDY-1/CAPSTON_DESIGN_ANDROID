package com.example.capstonedesign.data.response


import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("data")
    val data: ReviewData,
    @SerializedName("message")
    val message: String
)

data class UpdateReviewResponse(
    @SerializedName("data")
    val data: Content,
    @SerializedName("message")
    val message: String
)

data class SortX(
    @SerializedName("empty")
    val empty: Boolean,
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)

data class ReviewData(
    @SerializedName("content")
    val content: List<Content>,
    @SerializedName("empty")
    val empty: Boolean,
    @SerializedName("first")
    val first: Boolean,
    @SerializedName("last")
    val last: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("numberOfElements")
    val numberOfElements: Int,
    @SerializedName("pageable")
    val pageable: Pageable,
    @SerializedName("size")
    val size: Int,
    @SerializedName("sort")
    val sort: SortX,
    @SerializedName("totalElements")
    val totalElements: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)

data class Pageable(
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("paged")
    val paged: Boolean,
    @SerializedName("sort")
    val sort: SortX,
    @SerializedName("unpaged")
    val unpaged: Boolean
)

data class Content(
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("promotionId")
    val promotionId: Int,
    @SerializedName("star")
    val star: Int
)