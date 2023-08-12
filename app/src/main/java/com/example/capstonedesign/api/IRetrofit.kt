package com.example.capstonedesign.api


import com.example.capstonedesign.response.ItemResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IRetrofit {

    @GET("/konbini/items")
    suspend fun searchItems(
        @Query("name") name: String,
        @Query("strength") strength: String="STRONG"

    ): Response<ItemResponse>

    @Multipart
    @POST("/konbini/items/image")
    suspend fun postImage(
        @Part image: MultipartBody.Part
    ): Response<ItemResponse>
}