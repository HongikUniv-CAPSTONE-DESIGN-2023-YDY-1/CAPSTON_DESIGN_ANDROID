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
import com.example.capstonedesign.data.adapter.ItemSearchAdapter
import com.example.capstonedesign.data.adapter.ReviewAdapter
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.reviewViewModel.ReviewViewModel
import com.example.capstonedesign.data.reviewViewModel.ReviewViewModelProviderFactory
import com.example.capstonedesign.databinding.FragmentMapsBinding
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
            Log.d("토큰", accessToken)
            val accessTokenHeader = "Bearer ${accessToken}"
            Log.d("토큰헤더", accessTokenHeader)
            viewModel.getReviewByItemId(itemId!!.toInt(), 1, accessTokenHeader)
            viewModel.reviews.observe(viewLifecycleOwner, Observer { response ->
                when(response){
                    is Resource.Success -> {
                        response.data?.let { response ->
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
    }
    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.rvReview.layoutManager = LinearLayoutManager(activity)
        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.setHasFixedSize(true)
    }
}