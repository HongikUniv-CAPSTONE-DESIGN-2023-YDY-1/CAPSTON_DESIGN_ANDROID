package com.example.capstonedesign.data.api


import com.example.capstonedesign.data.response.PlaceInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KakaoInterface {

    @GET("/v2/local/search/keyword.{format}")
    fun getPlaceLatLng(
        @Path("format") format: String,
        @Query("y") y: Double,
        @Query("x") x: Double,
        @Query("radius") radius: Int,
        @Query("query") query: String,
        @Header("Authorization") authorization: String
    ): Call<PlaceInfo>
}