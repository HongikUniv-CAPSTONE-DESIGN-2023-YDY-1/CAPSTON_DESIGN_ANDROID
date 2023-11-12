package com.example.capstonedesign.data.response


data class AccessTokenResponse(
    val accessToken: String,
    val refreshToken: String

)

data class User(
    val email: String,
    val password: String,

    )

data class SignResponse(
    val data: AccessTokenResponse,
    val message: String
)

