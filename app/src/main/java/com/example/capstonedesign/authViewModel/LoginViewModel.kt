package com.example.capstonedesign.authViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstonedesign.repository.UserRepository
import com.example.capstonedesign.response.SignUpRequest
import com.example.capstonedesign.response.SignUpResponse
import com.example.capstonedesign.utils.BaseResponse
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val userRepo = UserRepository()
    val loginResult : MutableLiveData<BaseResponse<SignUpResponse>> = MutableLiveData()

    fun loginUser(email: String, password: String){
        loginResult.value =BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = SignUpRequest(email, password)
                val response =userRepo.loginUser(loginRequest)
                if (response!!.isSuccessful){
                    val accessToken = response.body()?.data?.accessToken
                    val refreshToken = response.body()?.data?.refreshToken
                    Log.d("로그인", "accessToken: $accessToken")
                    Log.d("로그인", "refreshToken: $refreshToken")
                    val sharedPreferences = getApplication<Application>().getSharedPreferences("user_Token", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("accessToken", accessToken)
                    editor.putString("refreshToken", refreshToken)
                    editor.apply()
                } else if (response.code() == 401) {
                    Log.d("401에러", response.message())
                } else{
                    Log.d("로그인 실패", "서버와 연결 실패")
                }
            }catch (e: Exception){
                loginResult.value = BaseResponse.Error(e.message)
                Log.d("연결실패", "onCreateView: ${e.message}")
            }
        }
    }
}