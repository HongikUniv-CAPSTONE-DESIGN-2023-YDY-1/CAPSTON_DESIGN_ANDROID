package com.example.capstonedesign.data.authViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstonedesign.data.repository.AuthRepository
import com.example.capstonedesign.data.repository.BaseRepository
import com.example.capstonedesign.data.response.SignResponse
import com.example.capstonedesign.utils.Resource
import com.example.capstonedesign.utils.SignResource
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _loginResponse : MutableLiveData<SignResource<SignResponse>> = MutableLiveData()
    val loginResponse: LiveData<SignResource<SignResponse>>
        get() = _loginResponse
    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = repository.login(email, password)
    }
}