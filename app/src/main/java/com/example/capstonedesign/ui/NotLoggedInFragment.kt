package com.example.capstonedesign.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentCameraBinding
import com.example.capstonedesign.databinding.FragmentHomeBinding
import com.example.capstonedesign.databinding.FragmentLoggedInBinding
import com.example.capstonedesign.databinding.FragmentNotLoggedInBinding

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
        return binding.root
    }


}