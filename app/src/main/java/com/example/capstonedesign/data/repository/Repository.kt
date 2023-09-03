package com.example.capstonedesign.data.repository


import com.example.capstonedesign.data.api.RetrofitInstance

import okhttp3.MultipartBody



class Repository {

    suspend fun getAllItems(name: String, strength: String) =
        RetrofitInstance.api.searchItems(name, strength)

    suspend fun uploadImgToServer(imgUrl: MultipartBody.Part) =
        RetrofitInstance.api.postImage(imgUrl)

}