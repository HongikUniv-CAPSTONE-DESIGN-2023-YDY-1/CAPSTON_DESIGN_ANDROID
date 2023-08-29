package com.example.capstonedesign.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.capstonedesign.MainActivity
import com.example.capstonedesign.R
import com.example.capstonedesign.api.RetrofitInstance
import com.example.capstonedesign.authViewModel.LoginViewModel
import com.example.capstonedesign.databinding.FragmentCameraBinding
import com.example.capstonedesign.databinding.FragmentHomeBinding
import com.example.capstonedesign.databinding.FragmentLoggedInBinding
import com.example.capstonedesign.databinding.FragmentNotLoggedInBinding
import com.example.capstonedesign.response.SignUpRequest
import kotlinx.coroutines.launch

class NotLoggedInFragment: Fragment(){

    private lateinit var binding: FragmentNotLoggedInBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotLoggedInBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.btnSignUp.setOnClickListener{
            findNavController().navigate(R.id.action_notLoggedInFragment_to_signUpFragment)
        }
        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            loginViewModel.loginUser(email, password)
        }
        return binding.root
    }


}