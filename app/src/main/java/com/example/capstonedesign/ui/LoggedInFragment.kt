package com.example.capstonedesign.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstonedesign.databinding.FragmentCameraBinding
import com.example.capstonedesign.databinding.FragmentHomeBinding
import com.example.capstonedesign.databinding.FragmentLoggedInBinding

class LoggedInFragment: Fragment(){

    private lateinit var binding: FragmentLoggedInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoggedInBinding.inflate(inflater, container, false)
        return binding?.root

    }


}