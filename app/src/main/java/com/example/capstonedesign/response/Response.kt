package com.example.capstonedesign.response


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("searchItems")
    val searchItems: List<SearchItem>
)