package com.example.capstonedesign.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.capstonedesign.R

import com.example.capstonedesign.databinding.FragmentMypageBinding

class MypageFragment: Fragment(){

    private lateinit var binding: FragmentMypageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding?.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isloggedIn = checkIsLoggedIn()

        if (isloggedIn) {
            binding.viewLoggedIn.visibility = View.VISIBLE
            binding.viewNotLoggedIn.visibility = View.GONE
        } else {
            binding.viewLoggedIn.visibility = View.GONE
            binding.viewNotLoggedIn.visibility = View.VISIBLE
        }
        binding.viewLoggedIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_loggedInFragment)
        }
        binding.viewNotLoggedIn.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_notLoggedInFragment)
        }
    }

    private fun checkIsLoggedIn(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("user_Token", 0)
        val accessToken = sharedPreferences.getString("accessToken", null)
        val refreshToken = sharedPreferences.getString("refreshToken", null)
        return accessToken != null && refreshToken != null
    }

}