package com.example.capstonedesign.repository

import com.example.capstonedesign.api.RetrofitInstance
import com.example.capstonedesign.response.SignUpRequest
import com.example.capstonedesign.response.SignUpResponse
import retrofit2.Response

class UserRepository {

    suspend fun loginUser(loginRequest:SignUpRequest): Response<SignUpResponse>? {
        return  RetrofitInstance.api.login(loginRequest)
    }
}