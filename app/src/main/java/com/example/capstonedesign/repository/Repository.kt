package com.example.capstonedesign.repository


import com.example.capstonedesign.api.RetrofitInstance
import com.example.capstonedesign.response.SignUpRequest
import com.example.capstonedesign.response.SignUpResponse
import okhttp3.MultipartBody



class Repository {

    suspend fun getAllItems(name: String, strength: String) =
        RetrofitInstance.api.searchItems(name, strength)

    suspend fun uploadImgToServer(imgUrl: MultipartBody.Part) =
        RetrofitInstance.api.postImage(imgUrl)

}