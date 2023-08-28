package com.example.capstonedesign.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.capstonedesign.R
import com.example.capstonedesign.api.RetrofitInstance
import com.example.capstonedesign.databinding.FragmentCameraBinding
import com.example.capstonedesign.databinding.FragmentHomeBinding
import com.example.capstonedesign.databinding.FragmentLoggedInBinding
import com.example.capstonedesign.databinding.FragmentNotLoggedInBinding
import com.example.capstonedesign.response.SignUpRequest
import kotlinx.coroutines.launch

class NotLoggedInFragment: Fragment(){

    private lateinit var binding: FragmentNotLoggedInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotLoggedInBinding.inflate(inflater, container, false)

        binding.signUpButton.setOnClickListener{
            findNavController().navigate(R.id.action_notLoggedInFragment_to_signUpFragment)
        }
        binding.loginButton.setOnClickListener{
            val email = "test2@test.com"
            val password = "qwer123!"
            val signUpRequest = SignUpRequest(email, password)
            val retrofit = RetrofitInstance.api
            viewLifecycleOwner.lifecycleScope.launch {

                try {
                    val response = retrofit.login(signUpRequest)
                    if (response.isSuccessful) {
                        val accessToken = response.body()?.data?.accessToken
                        val refreshToken = response.body()?.data?.refreshToken
                        Log.d("로그인", "onCreateView: $accessToken")
                        Log.d("로그인", "onCreateView: $refreshToken")
                    } else if (response.code() == 401) {
                        Log.d("로그인", "이메일 비번 틀림")
                    } else {
                        Log.d("로그인", "연결실패")
                    }
                } catch (e: Exception) {
                    Log.d("로그인", "onCreateView: ${e.message}")
                }
            }
        }
        return binding.root
    }


}