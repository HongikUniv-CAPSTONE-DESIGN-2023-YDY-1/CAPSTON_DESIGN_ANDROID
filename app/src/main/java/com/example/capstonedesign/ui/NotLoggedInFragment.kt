package com.example.capstonedesign.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        return binding?.root

    }


}