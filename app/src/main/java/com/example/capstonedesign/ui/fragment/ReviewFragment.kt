package com.example.capstonedesign.ui.fragment



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.data.adapter.ReviewAdapter
import com.example.capstonedesign.data.repository.Repository

import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModel
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModelProviderFactory
import com.example.capstonedesign.databinding.FragmentReviewBinding
import com.example.capstonedesign.utils.Resource
import com.example.capstonedesign.utils.UserPreferences

class ReviewFragment: Fragment(){

    private lateinit var binding: FragmentReviewBinding
    private lateinit var viewModel: ReviewViewModel
    private lateinit var userPreferences: UserPreferences
    private lateinit var reviewAdapter: ReviewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(inflater, container, false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemId = arguments?.getString("itemId")
        Log.d("itemId", itemId.toString())
        setupRecyclerView()
        val reviewRepository = Repository()
        val reviewViewModelProviderFactory = ReviewViewModelProviderFactory(reviewRepository)
        viewModel = ViewModelProvider(this, reviewViewModelProviderFactory).get(ReviewViewModel::class.java)
        userPreferences = UserPreferences(requireContext())


        userPreferences.accessToken.asLiveData().observe(viewLifecycleOwner) {
            val accessToken = it ?: ""
            Log.d("accessToken", accessToken)
            if (accessToken.isEmpty()) {
                Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                binding.tvNotLogin.visibility = View.VISIBLE
            } else {
                binding.tvNotLogin.visibility = View.GONE
            }
            val accessTokenHeader = "Bearer ${accessToken}"
            //val accessTokenHeader = "Bearer eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiJhY2Nlc3MiLCJ1c2VySUQiOjMsImV4cCI6MTcwMzMwNjg3MSwiaWF0IjoxNjk3MzA2ODcxLCJpc3MiOiJ0ZXN0In0.yIdX0F2ARracaiLMu1wCJGEAJGWeK-FgFOPeRhk4Wwc"
            viewModel.getReviewByItemId(itemId!!.toInt(), 1, accessTokenHeader)
            viewModel.reviews.observe(viewLifecycleOwner, Observer { response ->
                when(response){
                    is Resource.Success -> {
                        response.data?.let { response ->
                            Log.d("리뷰", "성공")
                            Log.d("리뷰", response.data.content.toString())
                            if (response.data.content.isEmpty()){
                                Toast.makeText(requireContext(), "리뷰가 없습니다.", Toast.LENGTH_SHORT).show()
                                reviewAdapter.differ.submitList(emptyList())
                            } else {
                                reviewAdapter.differ.submitList(response.data.content)
                            }
                        }
                    }
                    is  Resource.Error -> {
                        response.message?.let { message ->
                            Log.e("리뷰", "An error occured: $message")
                        }
                    }
                    is Resource.Loading -> {
                        Log.d("리뷰", "Loading...")
                    }
                }
            })
        }

        binding.btnReviewSubmit.setOnClickListener {
            userPreferences.accessToken.asLiveData().observe(viewLifecycleOwner){
                val accessToken = it ?:""
                val accessTokenHeader = "Bearer ${accessToken}"
                val promtionId = itemId!!.toInt()
                val content = binding.etContent.text.toString()
                val rating = binding.ratingBar.rating.toInt()

                viewModel.postReview(promtionId, rating , content, accessTokenHeader)
                Log.d("리뷰작성", "등록")
                Log.d("accessToken", accessTokenHeader)
                viewModel.reviewPost.observe(viewLifecycleOwner, Observer { response ->
                    when(response){
                        is Resource.Success -> {
                            response.data?.let { response ->
                                Log.d("리뷰작성", "등록됨")
                                Log.d("리뷰작성", response.data.toString())
                                Toast.makeText(requireContext(), "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                                binding.etContent.setText("")
                                viewModel.getReviewByItemId(promtionId, 1, accessTokenHeader)
                            }
                        }
                        is  Resource.Error -> {
                            response.message?.let { message ->
                                Log.e("리뷰작성", "An error occured: $message")
                            }


                        }
                        is Resource.Loading -> {
                            Log.d("리뷰작성", "Loading...")
                        }

                    }
                })
            }

        }

    }
    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.rvReview.layoutManager = LinearLayoutManager(activity)
        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.setHasFixedSize(true)
    }
}