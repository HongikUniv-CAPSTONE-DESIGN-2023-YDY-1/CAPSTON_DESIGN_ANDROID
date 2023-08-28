package com.example.capstonedesign.response



data class AccessTokenResponse(
    val accessToken: String,
    val refreshToken: String

)
data class SignUpRequest(
    val email: String,
    val password: String,

)
data class SignUpResponse(
    val data: AccessTokenResponse,
    val message: String
)
