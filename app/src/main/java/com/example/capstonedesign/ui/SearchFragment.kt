package com.example.capstonedesign.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.MainActivity
import com.example.capstonedesign.adapter.ItemSearchAdapter
import com.example.capstonedesign.databinding.FragmentSearchBinding
import com.example.capstonedesign.model.ItemSearchViewModel
import com.example.capstonedesign.response.ItemResponse
import com.example.capstonedesign.utils.Resource


class SearchFragment: Fragment(){

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: ItemSearchAdapter
    lateinit var viewModel: ItemSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.allItems.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {response ->
                        Log.d("검색프레그먼트", "response: $response")
                        adapter.differ.submitList(response.response.searchItems)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("검색프레그먼트", "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })



        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.allItems.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {response ->
                        Log.d("검색프레그먼트", "response: $response")
                        adapter.differ.submitList(response.response.searchItems)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("검색프레그먼트", "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun setupRecyclerView() {
        adapter = ItemSearchAdapter()
        binding.rvItemList.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}