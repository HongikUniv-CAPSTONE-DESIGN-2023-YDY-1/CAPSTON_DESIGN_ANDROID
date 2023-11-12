package com.example.capstonedesign.utils

import okhttp3.ResponseBody

sealed class SignResource<out T> {
    data class Success<out T>(val value: T) : SignResource<T>()
    data class Failure(
        val isNetworkError: Boolean, val errorCode: Int?, val errorMessage: ResponseBody?
    ) : SignResource<Nothing>()
}
