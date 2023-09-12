package com.example.capstonedesign.data.response



data class ChangePasswordResponse(

    val data: Data,
    val message: String
)
data class Data(
    val result: Boolean
)
data class PasswordChangeBody(
    val email: String,
    val password: String,
    val newPassword: String
)