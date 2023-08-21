package com.example.capstonedesign.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.capstonedesign.MainActivity
import com.example.capstonedesign.databinding.FragmentCameraBinding
import com.example.capstonedesign.databinding.FragmentCameraResultBinding
import com.example.capstonedesign.databinding.FragmentHomeBinding
import com.example.capstonedesign.model.ItemSearchViewModel

class CameraResultFragment: Fragment(){

    private lateinit var binding: FragmentCameraResultBinding
    lateinit var viewModel: ItemSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraResultBinding.inflate(inflater, container, false)


        val input = requireArguments().getString("capturedPhoto")




        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        viewModel.Items.observe(viewLifecycleOwner, Observer { response ->
            when


        })
    }


}