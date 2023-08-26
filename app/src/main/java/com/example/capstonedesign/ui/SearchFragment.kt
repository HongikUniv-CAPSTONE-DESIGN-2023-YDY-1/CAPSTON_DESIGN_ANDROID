package com.example.capstonedesign.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.MainActivity
import com.example.capstonedesign.R
import com.example.capstonedesign.adapter.ItemSearchAdapter
import com.example.capstonedesign.databinding.FragmentSearchBinding
import com.example.capstonedesign.model.ItemSearchViewModel
import com.example.capstonedesign.response.ItemResponse
import com.example.capstonedesign.utils.Resource


class SearchFragment: Fragment(){

    private lateinit var binding: FragmentSearchBinding
    private lateinit var itemadapter: ItemSearchAdapter
    lateinit var viewModel: ItemSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.Items.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {response ->
                        Log.d("검색프레그먼트", "response: $response")
                        if (response.response.searchItems.isEmpty()) {

                            Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()

                            itemadapter.differ.submitList(emptyList())
                        } else {
                            itemadapter.differ.submitList(response.response.searchItems)
                        }
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
        binding.btnItemSearch.setOnClickListener {
            val searchWord = binding.etItemSearch.text.toString()

            if (searchWord.isEmpty()) {
                binding.etItemSearch.error = "검색어를 입력해주세요."
                return@setOnClickListener
            } else {
                viewModel.searchItems(searchWord, "STRONG")
            }
        }
        binding.btnAll.setOnClickListener {
            viewModel.getAllItems()
        }
        binding.btnOnePlusOne.setOnClickListener {
            viewModel.searchPromotion("ONE_PLUS_ONE")
        }
        binding.btnTwoPlusOne.setOnClickListener {
            viewModel.searchPromotion("TWO_PLUS_ONE")
        }
        itemadapter.setOnItemLongClickListener {
            val bundle = Bundle().apply {
                putSerializable("item", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_itemDetailFragmnet,
                bundle
            )
        }

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun setupRecyclerView() {
        itemadapter = ItemSearchAdapter()
        binding.rvItemList.layoutManager = LinearLayoutManager(activity)
        binding.rvItemList.adapter = itemadapter
        binding.rvItemList.setHasFixedSize(true)
    }
}