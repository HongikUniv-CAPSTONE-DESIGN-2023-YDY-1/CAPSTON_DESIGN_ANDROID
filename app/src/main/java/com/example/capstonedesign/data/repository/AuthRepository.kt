package com.example.capstonedesign.data.repository

import com.example.capstonedesign.data.api.IRetrofit
import com.example.capstonedesign.data.response.ChangePasswordResponse
import com.example.capstonedesign.data.response.PasswordChangeBody
import com.example.capstonedesign.data.response.User

class AuthRepository(
    private val api: IRetrofit
): BaseRepository() {

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        val user: User = User(email, password)
        api.login(user)

    }
    suspend fun ChangePassword(
        email: String,
        password: String,
        newPassword: String
    ) = safeApiCall {
        val passwordChangeBody = PasswordChangeBody(email, password, newPassword)
        api.changePassWord(passwordChangeBody)
    }
}