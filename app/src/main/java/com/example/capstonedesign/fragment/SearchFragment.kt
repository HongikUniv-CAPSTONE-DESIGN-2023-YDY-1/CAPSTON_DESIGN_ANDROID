package com.example.capstonedesign.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstonedesign.databinding.FragmentSearchBinding

class SearchFragment: Fragment(){

    private var mbinding: FragmentSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        mbinding = binding
        return mbinding?.root

    }

    override fun onDestroyView() {
        mbinding = null
        super.onDestroyView()
    }
}