package com.example.capstonedesign.model

import android.telephony.CellSignalStrength
import com.example.capstonedesign.api.RetrofitInstance

class Repository {

    suspend fun getAllItems(name: String, strength: String) =
        RetrofitInstance.api.searchItems(name, strength)
}