package com.example.capstonedesign.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object KakaoClient {
    //레트로핏 클라이언트 선언

    private var retrofitclient: Retrofit? = null

    //레트로핏 클라이언트 가져오기
    fun getClient(baseUrl: String): Retrofit? {
        if (retrofitclient == null) {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build()

            retrofitclient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return retrofitclient
    }

}

