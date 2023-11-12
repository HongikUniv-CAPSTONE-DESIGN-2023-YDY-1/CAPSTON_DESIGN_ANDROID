package com.example.capstonedesign.ui.activity

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import com.example.capstonedesign.R
import com.example.capstonedesign.data.api.RetrofitInstance
import com.example.capstonedesign.data.response.User
import com.example.capstonedesign.databinding.ActivitySignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEmail.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this
        binding.etPasswordConfirm.onFocusChangeListener = this


        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val signUpRequest = User(email = email, password = password)
            val retrofit = RetrofitInstance.api
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = retrofit.signUp(signUpRequest)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val accessToken = response.body()?.data?.accessToken
                            val refreshToken = response.body()?.data?.refreshToken
                            Log.d("회원가입", "onCreateView: $accessToken")
                            Log.d("회원가입", "onCreateView: $refreshToken")
                            showSignUpSuccessDialog()
                        } else if (response.code() == 400) {
                            Log.d("회원가입 400", "${response.errorBody()}")
                            showSignUPFailDialog()
                        } else if (response.code() == 409) {
                            Log.d("회원가입 409", "${response.errorBody()}")
                            showSignUPFailDialog()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("회원가입", "onCreateView: ${e.message}")
                }
            }


        }
        binding.ibGoBack.setOnClickListener {
            finish()
        }
    }

    private fun showSignUpSuccessDialog() {
        val title = "회원가입"
        val message = "회원가입에 성공했습니다."
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("로그인하러가기") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            show()
        }
    }

    private fun showSignUPFailDialog() {
        val title = "회원가입"
        val message = "이미 가입된 이메일입니다."
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                resetEditText()
            }
            show()
        }
    }

    private fun resetEditText() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etPasswordConfirm.text?.clear()


    }


    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val email = binding.etEmail.text.toString()
        if (email.isEmpty()) {
            errorMessage = "이메일을 입력해주세요."
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "이메일 형식이 올바르지 않습니다."
        }

        if (errorMessage != null) {
            binding.emailTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val password = binding.etPassword.text.toString()
        if (password.isEmpty()) {
            errorMessage = "비밀번호를 입력해주세요."
        } else if (password.length < 8) {
            errorMessage = "비밀번호는 8자리 이상이어야 합니다."
        } else if (password.length > 20) {
            errorMessage = "비밀번호는 20자리 이하이어야 합니다."
        } else if (!password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&*?!_~])[A-Za-z\\d@#\$%^&*?!_~]{8,20}\$"))) {
            errorMessage = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
        }
        if (errorMessage != null) {
            binding.passwordTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password = binding.etPassword.text.toString()
        val passwordCheck = binding.etPasswordConfirm.text.toString()
        if (passwordCheck.isEmpty()) {
            errorMessage = "확인 비밀번호를 입력해주세요."
        } else if (password.length < 8) {
            errorMessage = "확인 비밀번호는 8자리 이상이어야 합니다."
        } else if (password.length > 20) {
            errorMessage = "확인 비밀번호는 20자리 이하이어야 합니다."
        } else if (!password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&*!?_~])[A-Za-z\\d@#\$%^&*!?_~]{8,20}\$"))) {
            errorMessage = "확인 비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
        }
        if (errorMessage != null) {
            binding.passwordConfirmTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordConfirm.text.toString()
        if (password != confirmPassword) {
            errorMessage = "비밀번호가 일치하지 않습니다."
        }
        if (errorMessage != null) {
            binding.passwordConfirmTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }


    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.et_email -> {
                    if (hasFocus) {
                        if (binding.emailTil.isErrorEnabled) {
                            binding.emailTil.isErrorEnabled = false
                        }
                    } else {
                        if (validateEmail()) {
                            binding.emailTil.apply {
                                setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }

                R.id.et_password -> {
                    if (hasFocus) {
                        if (binding.passwordTil.isErrorEnabled) {
                            binding.passwordTil.isErrorEnabled = false
                        }
                    } else {
                        if (validatePassword() && binding.etPasswordConfirm.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                            if (binding.passwordConfirmTil.isErrorEnabled) {
                                binding.passwordConfirmTil.isErrorEnabled = false
                            }
                            binding.passwordTil.apply {
                                setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }

                R.id.et_password_confirm -> {
                    if (hasFocus) {
                        if (binding.passwordConfirmTil.isErrorEnabled) {
                            binding.passwordConfirmTil.isErrorEnabled = false
                        }
                    } else {
                        if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()) {
                            if (binding.passwordTil.isErrorEnabled) {
                                binding.passwordTil.isErrorEnabled = false
                            }
                            binding.passwordConfirmTil.apply {
                                setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKey(veiw: View?, event: Int, keyEvent: KeyEvent?): Boolean {

        return false
    }
}