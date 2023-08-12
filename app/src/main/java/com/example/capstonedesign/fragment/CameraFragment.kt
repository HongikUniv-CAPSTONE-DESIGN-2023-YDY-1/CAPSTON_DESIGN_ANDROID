package com.example.capstonedesign.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstonedesign.databinding.FragmentCameraBinding
import com.example.capstonedesign.databinding.FragmentHomeBinding

class CameraFragment: Fragment(){

    private var mbinding: FragmentCameraBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCameraBinding.inflate(inflater, container, false)
        mbinding = binding
        return mbinding?.root

    }

    override fun onDestroyView() {
        mbinding = null
        super.onDestroyView()
    }
}