package com.example.capstonedesign.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModel
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModelProviderFactory
import com.example.capstonedesign.databinding.ActivityUpdateReviewBinding
import com.example.capstonedesign.utils.Resource
import com.example.capstonedesign.utils.UserPreferences

class UpdateReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateReviewBinding
    private lateinit var userPreferences: UserPreferences
    lateinit var viewModel: ReviewViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.ibGoBack.setOnClickListener {
            finish()
        }

        val reviewId = intent.getIntExtra("reviewId", 0)
        val reviewContent = intent.getStringExtra("reviewContent")
        val reviewRating = intent.getFloatExtra("reviewRating", 0.0f)
        val promotionId = intent.getIntExtra("promotionId", 0)


        userPreferences = UserPreferences(this)
        val reviewRepository = Repository()
        val reviewViewModelProviderFactory = ReviewViewModelProviderFactory(reviewRepository)
        viewModel =
            ViewModelProvider(this, reviewViewModelProviderFactory).get(ReviewViewModel::class.java)

        binding.etUpdateContent.setText(reviewContent)
        binding.ratingBar.rating = reviewRating
        binding.tvContentId.text = "리뷰${reviewId}"
        binding.btnUpdateReview.setOnClickListener {
            userPreferences.accessToken.asLiveData().observe(this) {
                val accessToken = it ?: ""
                val accessTokenHeader = "Bearer ${accessToken}"
                val rating = binding.ratingBar.rating
                val reviewContent = binding.etUpdateContent.text.toString()

                viewModel.updateReview(
                    reviewId,
                    promotionId,
                    rating.toInt(),
                    reviewContent,
                    accessTokenHeader
                )
                viewModel.updateReview.observe(this, Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let { updateReviewResponse ->
                                Log.d("UpdateReviewActivity", "onCreate: ${updateReviewResponse}")
                                Toast.makeText(this, "리뷰가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                Log.d("UpdateReviewActivity", "onCreate: ${message}")
                                Toast.makeText(this, "리뷰 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }


    }


}