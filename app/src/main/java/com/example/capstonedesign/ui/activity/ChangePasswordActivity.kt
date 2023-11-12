package com.example.capstonedesign.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.capstonedesign.data.api.IRetrofit
import com.example.capstonedesign.data.api.RemoteDataSource
import com.example.capstonedesign.data.viewModel.auth.AuthViewModel
import com.example.capstonedesign.data.base.ViewModelFactory
import com.example.capstonedesign.data.repository.AuthRepository
import com.example.capstonedesign.utils.UserPreferences
import com.example.capstonedesign.databinding.ActivityChangePasswordBinding
import com.example.capstonedesign.utils.SignResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding
    lateinit var userPreferences: UserPreferences
    lateinit var viewModel: AuthViewModel
    val remoteDataSource = RemoteDataSource()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)
        presentPassWorFocusListener()
        newPasswordFocusListener()

        binding.btnChangePassword.setOnClickListener {
            changePassword()


        }
        binding.ibGoBack.setOnClickListener {
            finish()
        }


        val factory = AuthRepository(remoteDataSource.buildApi(IRetrofit::class.java))
        viewModel =
            ViewModelProvider(this, ViewModelFactory(factory)).get(AuthViewModel::class.java)

        viewModel.passwordChangeResponse.observe(this, Observer {
            when (it) {
                is SignResource.Success -> {
                    lifecycleScope.launch {
                        userPreferences.saveNewPassword(
                            binding.etNewPassword.text.toString().trim()
                        )
                    }
                    Toast.makeText(this, "비밀번호 변경 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }

                is SignResource.Failure -> {
                    Log.d("ChangePasswordActivity", "onCreate: ${it}")
                    Toast.makeText(this, "비밀번호 변경 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun changePassword() {
        binding.presentPasswordTil.helperText = validPassword()
        binding.newPasswordTil.helperText = validNewPassword()
        val validPresentPassword = binding.presentPasswordTil.helperText == null
        val validNewPassword = binding.newPasswordTil.helperText == null


        if (validNewPassword && validPresentPassword)
            resetForm()
        else
            invalidFrom()
    }

    private fun invalidFrom() {
        var message = ""
        if (binding.presentPasswordTil.helperText != null)
            message += "현재 비밀번호: ${binding.presentPasswordTil.helperText}\n"
        if (binding.newPasswordTil.helperText != null)
            message += "새 비밀번호: ${binding.newPasswordTil.helperText}\n"

        AlertDialog.Builder(this)
            .setTitle("비밀번호 변경 실패")
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->

            }
            .show()


    }

    private fun resetForm() {
        val message = "현재 비밀번호:" + binding.etPresentPassword.text.toString() +
                "\n새 비밀번호:" + binding.etNewPassword.text.toString()

        AlertDialog.Builder(this)
            .setTitle("비밀번호 변경")
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->
                CoroutineScope(lifecycleScope.coroutineContext).launch {
                    val email = userPreferences.userEmail.asLiveData().value ?: ""
                    val password = userPreferences.userPassword.asLiveData().value ?: ""
                    Log.d("ChangePasswordActivity", "resetForm: $email, $password")
                    Log.d(
                        "ChangePasswordActivity",
                        "resetForm: ${binding.etNewPassword.text.toString().trim()}"
                    )
                    viewModel.changePassword(
                        email,
                        password,
                        binding.etNewPassword.text.toString().trim()
                    )
                }

            }
            .show()
    }


    private fun presentPassWorFocusListener() {
        binding.etPresentPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.presentPasswordTil.helperText = validPassword()
            }
        }

    }

    private fun validPassword(): String? {
        val password = binding.etPresentPassword.text.toString()
        userPreferences.userPassword.asLiveData().observe(this) {
            val userPassword = it ?: ""
            if (password != userPassword) {
                binding.presentPasswordTil.error = "비밀번호가 일치하지 않습니다."
            } else {
                binding.presentPasswordTil.error = null
            }
        }

        return null
    }

    private fun newPasswordFocusListener() {
        binding.etNewPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.newPasswordTil.helperText = validNewPassword()
            }
        }
    }

    private fun validNewPassword(): String? {
        val newPassWord = binding.etNewPassword.text.toString()
        if (newPassWord.length < 8) {
            return "비밀번호는 8자리 이상이어야 합니다."
        }
        if (newPassWord.length > 20) {
            return "비밀번호는 20자리 이하이어야 합니다."
        }
        if (newPassWord.isEmpty()) {
            return "비밀번호를 입력해주세요."
        }
        if (!newPassWord.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&*?!_~])[A-Za-z\\d@#\$%^&*?!_~]{8,20}\$"))) {
            return "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
        }
        return null
    }

}