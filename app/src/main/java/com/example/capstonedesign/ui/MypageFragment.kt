package com.example.capstonedesign.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.example.capstonedesign.R
import com.example.capstonedesign.data.UserPreferences

import com.example.capstonedesign.databinding.FragmentMypageBinding

class MypageFragment: Fragment(){

    private lateinit var binding: FragmentMypageBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())

        userPreferences.loginStatus.asLiveData().observe(viewLifecycleOwner) {
            val logInStatus = it ?: false
            if (logInStatus) {
                binding.viewLoggedIn.visibility = View.VISIBLE
                binding.viewNotLoggedIn.visibility = View.GONE
            } else {
                binding.viewLoggedIn.visibility = View.GONE
                binding.viewNotLoggedIn.visibility = View.VISIBLE
            }
        }
        userPreferences.userEmail.asLiveData().observe(viewLifecycleOwner) {
            val email = it ?: ""
            binding.tvUserEmail.text = "$email 님 안녕하세요!!"
        }

        binding.viewLoggedIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_loggedInFragment)
        }
        binding.viewNotLoggedIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_notLoggedInFragment)
        }


    }

    




}