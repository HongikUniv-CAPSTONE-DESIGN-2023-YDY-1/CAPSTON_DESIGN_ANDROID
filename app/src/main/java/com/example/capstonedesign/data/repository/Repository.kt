package com.example.capstonedesign.data.repository


import com.example.capstonedesign.data.api.RetrofitInstance
import com.example.capstonedesign.data.response.ReviewPostBody
import com.example.capstonedesign.data.response.UpdateReviewBody
import okhttp3.MultipartBody


class Repository {

    suspend fun getAllItems(name: String, strength: String) =
        RetrofitInstance.api.searchItems(name, strength)

    suspend fun uploadImgToServer(imgUrl: MultipartBody.Part) =
        RetrofitInstance.api.postImage(imgUrl)

    suspend fun getReviewByItemId(itemID: Int, page: Int, accessToken: String) =
        RetrofitInstance.api.getReviewByItemId(itemID, page, accessToken)

    suspend fun getReviewByUserId(page: Int, accessToken: String) =
        RetrofitInstance.api.getReviewByUserId(page, accessToken)

    suspend fun reviewPost(reviewPostBody: ReviewPostBody, accessToken: String) =
        RetrofitInstance.api.postReview(reviewPostBody, accessToken)

    suspend fun updateReview(updateReviewBody: UpdateReviewBody, accessToken: String) =
        RetrofitInstance.api.updateReview(updateReviewBody, accessToken)

    suspend fun getRecommendList(accessToken: String) =
        RetrofitInstance.api.getRecommendList(accessToken)
}