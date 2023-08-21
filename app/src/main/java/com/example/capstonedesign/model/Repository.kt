package com.example.capstonedesign.model

import android.telephony.CellSignalStrength
import com.example.capstonedesign.api.RetrofitInstance
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Repository {

    suspend fun getAllItems(name: String, strength: String) =
        RetrofitInstance.api.searchItems(name, strength)

    suspend fun uploadImgToServer(imgUrl: String) {
        val file = File(imgUrl)
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
    }

}