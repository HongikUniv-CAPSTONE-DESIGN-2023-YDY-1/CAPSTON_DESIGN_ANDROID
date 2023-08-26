package com.example.capstonedesign.ui

import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstonedesign.R

import com.example.capstonedesign.databinding.FragmentSignUpBinding

class SignUpFragment: Fragment(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.etEmail.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this
        binding.etPasswordConfirm.onFocusChangeListener = this
        return binding.root

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
        } else if(password.length < 8) {
            errorMessage = "비밀번호는 8자리 이상이어야 합니다."
        } else if (password.length> 20) {
            errorMessage = "비밀번호는 20자리 이하이어야 합니다."
        } else if (!password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&*?_~])[A-Za-z\\d@#\$%^&*?_~]{8,20}\$"))) {
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
        } else if(password != passwordCheck) {
            errorMessage = "확인 비밀번호와 비밀번호가 일치하지 않습니다."
        }
        if (errorMessage != null) {
            binding.passwordConfirmTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }
    override fun onClick(view: View?) {

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null){
            when (view.id) {
                R.id.et_email -> {
                    if (hasFocus) {
                        if (binding.emailTil.isErrorEnabled) {
                            binding.emailTil.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }
                R.id.et_password -> {
                    if (hasFocus) {
                        if (binding.passwordTil.isErrorEnabled) {
                            binding.passwordTil.isErrorEnabled = false
                        }
                    } else {
                        validatePassword()
                    }
                }
                R.id.et_password_confirm -> {
                    if (hasFocus) {
                        if (binding.passwordConfirmTil.isErrorEnabled) {
                            binding.passwordConfirmTil.isErrorEnabled = false
                        }
                    } else {
                        validateConfirmPassword()
                    }
                }
            }
        }
    }

    override fun onKey(veiw: View?, event: Int, keyEvent: KeyEvent?): Boolean {

        return false
    }


}