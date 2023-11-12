package com.example.capstonedesign.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.data.adapter.ReviewAdapter
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModel
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModelProviderFactory
import com.example.capstonedesign.databinding.ActivityMyReviewBinding
import com.example.capstonedesign.utils.Resource
import com.example.capstonedesign.utils.UserPreferences

class MyReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyReviewBinding
    private lateinit var userPreferences: UserPreferences
    lateinit var viewModel: ReviewViewModel
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ibGoBack.setOnClickListener {
            finish()
        }
        setupRecyclerView()
        userPreferences = UserPreferences(this)
        val reviewRepository = Repository()
        val reviewViewModelProviderFactory = ReviewViewModelProviderFactory(reviewRepository)
        viewModel =
            ViewModelProvider(this, reviewViewModelProviderFactory).get(ReviewViewModel::class.java)
        userPreferences.accessToken.asLiveData().observe(this) {
            val accessToken = it ?: ""
            val accessTokenHeader = "Bearer ${accessToken}"
            viewModel.getReviewByUserId(1, accessTokenHeader)
            viewModel.reviewByUserId.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { reviewResponse ->
                            Log.d("reviewResponse", reviewResponse.toString())
                            Log.d("reviewResponse", reviewResponse.data.content.toString())
                            if (reviewResponse.data.content.isEmpty()) {
                                Toast.makeText(this, "작성한 리뷰가 없습니다.", Toast.LENGTH_SHORT).show()
                                reviewAdapter.differ.submitList(emptyList())
                            } else {
                                reviewAdapter.differ.submitList(reviewResponse.data.content)

                            }

                        }

                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                            println("An error occured: $message")
                        }
                    }

                    is Resource.Loading -> {
                        Log.d("리뷰", "로딩")
                    }
                }
            })
        }

        reviewAdapter.setOnItemClickListener {
            val intent = Intent(this, UpdateReviewActivity::class.java)
            intent.putExtra("reviewId", it.commentId)
            intent.putExtra("reviewContent", it.content)
            intent.putExtra("reviewRating", it.star)
            intent.putExtra("promotionId", it.promotionId)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        userPreferences.accessToken.asLiveData().observe(this) {
            val accessToken = it ?: ""
            val accessTokenHeader = "Bearer ${accessToken}"
            viewModel.getReviewByUserId(1, accessTokenHeader)
            viewModel.reviewByUserId.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { reviewResponse ->
                            Log.d("reviewResponse", reviewResponse.toString())
                            Log.d("reviewResponse", reviewResponse.data.content.toString())
                            if (reviewResponse.data.content.isEmpty()) {
                                Toast.makeText(this, "작성한 리뷰가 없습니다.", Toast.LENGTH_SHORT).show()
                                reviewAdapter.differ.submitList(emptyList())
                            } else {
                                reviewAdapter.differ.submitList(reviewResponse.data.content)

                            }

                        }

                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                            println("An error occured: $message")
                        }
                    }

                    is Resource.Loading -> {
                        Log.d("리뷰", "로딩")
                    }
                }
            })
        }
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.rvMyReview.layoutManager = LinearLayoutManager(this)
        binding.rvMyReview.adapter = reviewAdapter
        binding.rvMyReview.setHasFixedSize(true)
    }
}