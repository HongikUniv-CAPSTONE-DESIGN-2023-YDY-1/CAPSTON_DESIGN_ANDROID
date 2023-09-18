package com.example.capstonedesign.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.capstonedesign.utils.UserPreferences
import com.example.capstonedesign.data.api.IRetrofit
import com.example.capstonedesign.data.api.RemoteDataSource
import com.example.capstonedesign.data.authViewModel.AuthViewModel
import com.example.capstonedesign.data.base.ViewModelFactory
import com.example.capstonedesign.data.repository.AuthRepository
import com.example.capstonedesign.databinding.ActivityLogInBinding
import com.example.capstonedesign.utils.SignResource
import kotlinx.coroutines.launch

class LogInActivity() : AppCompatActivity() {

   private lateinit var binding: ActivityLogInBinding
   lateinit var userPreferences: UserPreferences
   lateinit var viewModel: AuthViewModel
   val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userPreferences = UserPreferences(this)
        val factory = AuthRepository(remoteDataSource.buildApi(IRetrofit::class.java))
        viewModel = ViewModelProvider(this, ViewModelFactory(factory)).get(AuthViewModel::class.java)


        viewModel.loginResponse.observe(this, Observer {
            when(it){
                is SignResource.Success ->{
                    lifecycleScope.launch {
                        userPreferences.saveToken(it.value.data.accessToken, it.value.data.refreshToken)
                        userPreferences.saveEmailAndPassword(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
                        userPreferences.saveLoginStatus(true)
                    }
                    Toast.makeText(this, "로그인성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is SignResource.Failure ->{
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.btnLogIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        binding.ibGoBack.setOnClickListener {
            finish()
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}