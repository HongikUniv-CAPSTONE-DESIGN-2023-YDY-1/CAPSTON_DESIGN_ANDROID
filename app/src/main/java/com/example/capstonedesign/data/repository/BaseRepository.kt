package com.example.capstonedesign.data.repository

import com.example.capstonedesign.utils.SignResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): SignResource<T> {
        return withContext(Dispatchers.IO) {
            try {
                SignResource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        SignResource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody()
                        )
                    }

                    else -> {
                        SignResource.Failure(true, null, null)
                    }
                }
            }
        }
    }
}