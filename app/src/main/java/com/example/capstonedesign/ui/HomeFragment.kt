package com.example.capstonedesign.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.capstonedesign.R
import com.example.capstonedesign.api.KakaoClient
import com.example.capstonedesign.api.KakaoInterface
import com.example.capstonedesign.databinding.FragmentHomeBinding
import com.example.capstonedesign.response.PlaceInfo
import com.example.capstonedesign.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment: Fragment(){

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnMypage.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mypageFragment)
        }



        return binding.root
    }


}