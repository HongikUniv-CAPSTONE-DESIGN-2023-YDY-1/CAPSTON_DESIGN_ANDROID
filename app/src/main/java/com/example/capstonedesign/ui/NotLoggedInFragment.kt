package com.example.capstonedesign.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.capstonedesign.R
import com.example.capstonedesign.data.api.IRetrofit
import com.example.capstonedesign.data.authViewModel.AuthViewModel
import com.example.capstonedesign.data.base.BaseFragment
import com.example.capstonedesign.databinding.FragmentNotLoggedInBinding
import com.example.capstonedesign.data.repository.AuthRepository
import com.example.capstonedesign.utils.SignResource
import kotlinx.coroutines.launch


class NotLoggedInFragment: BaseFragment<AuthViewModel, FragmentNotLoggedInBinding, AuthRepository>(){


    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotLoggedInBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(remoteDataSource.buildApi(IRetrofit::class.java))


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is SignResource.Success ->{
                    lifecycleScope.launch {
                        userPreferences.saveToken(it.value.data.accessToken, it.value.data.refreshToken)
                        userPreferences.saveEmailAndPassword(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
                    }
                    findNavController().navigate(R.id.action_notLoggedInFragment_to_mypageFragment)
                    Toast.makeText(requireContext(), "로그인성공", Toast.LENGTH_SHORT).show()
                }
                is SignResource.Failure ->{
                    Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
        binding.btnSignUp.setOnClickListener{
            findNavController().navigate(R.id.action_notLoggedInFragment_to_signUpFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }
    }


}