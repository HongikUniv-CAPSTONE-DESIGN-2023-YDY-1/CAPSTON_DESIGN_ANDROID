package com.example.capstonedesign.data.api


import com.example.capstonedesign.data.response.ChangePasswordResponse
import com.example.capstonedesign.data.response.ItemResponse
import com.example.capstonedesign.data.response.PasswordChangeBody
import com.example.capstonedesign.data.response.RecommendListResponse
import com.example.capstonedesign.data.response.ReviewPostBody
import com.example.capstonedesign.data.response.ReviewPostResponse
import com.example.capstonedesign.data.response.ReviewResponse
import com.example.capstonedesign.data.response.SignResponse
import com.example.capstonedesign.data.response.UpdateReviewBody
import com.example.capstonedesign.data.response.UpdateReviewResponse
import com.example.capstonedesign.data.response.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface IRetrofit {

    @GET("/konbini/items")
    suspend fun searchItems(
        @Query("name") name: String,
        @Query("strength") strength: String = "STRONG"

    ): Response<ItemResponse>

    @Multipart
    @POST("/konbini/items/image")
    suspend fun postImage(
        @Part image: MultipartBody.Part
    ): Response<ItemResponse>

    @POST("/member")
    suspend fun signUp(
        @Body signUpRequest: User
    ): Response<SignResponse>

    @POST("/member/login")
    suspend fun login(
        @Body signInRequest: User
    ): SignResponse

    @PATCH("/member/password")
    suspend fun changePassWord(
        @Body updateRequest: PasswordChangeBody
    ): ChangePasswordResponse

    @GET("/comment/list/{itemID}/{page}")
    suspend fun getReviewByItemId(

        @Path("itemID") itemID: Int,
        @Path("page") page: Int,
        @Header("Authorization") accessToken: String
    ): Response<ReviewResponse>

    @GET("/comment/list/{page}")
    suspend fun getReviewByUserId(
        @Path("page") page: Int,
        @Header("Authorization") accessToken: String,
    ): Response<ReviewResponse>

    @POST("/comment")
    suspend fun postReview(
        @Body reviewPostBody: ReviewPostBody,
        @Header("Authorization") accessToken: String
    ): Response<ReviewPostResponse>

    @PUT("/comment")
    suspend fun updateReview(
        @Body updateReviewBody: UpdateReviewBody,
        @Header("Authorization") accessToken: String
    ): Response<UpdateReviewResponse>

    @GET("/member/promotions/recommend")
    suspend fun getRecommendList(
        @Header("Authorization") accessToken: String
    ): Response<RecommendListResponse>

}
