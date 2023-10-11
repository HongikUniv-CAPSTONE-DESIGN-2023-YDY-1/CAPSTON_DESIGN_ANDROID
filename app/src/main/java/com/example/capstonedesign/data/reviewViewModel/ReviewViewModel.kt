package com.example.capstonedesign.data.reviewViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.response.ReviewPostBody
import com.example.capstonedesign.data.response.ReviewPostResponse
import com.example.capstonedesign.data.response.ReviewResponse
import com.example.capstonedesign.data.response.UpdateReviewBody
import com.example.capstonedesign.data.response.UpdateReviewResponse
import com.example.capstonedesign.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ReviewViewModel(
    val reviewRepository: Repository
): ViewModel() {

    val reviews : MutableLiveData<Resource<ReviewResponse>> = MutableLiveData()
    val reviewPost : MutableLiveData<Resource<ReviewPostResponse>> = MutableLiveData()
    val reviewByUserId : MutableLiveData<Resource<ReviewResponse>> = MutableLiveData()
    val updateReview : MutableLiveData<Resource<UpdateReviewResponse>> = MutableLiveData()



    fun getReviewByItemId(itemID: Int, page: Int ,accessToken: String)  = viewModelScope.launch {
        reviews.postValue(Resource.Loading())
        val response = reviewRepository.getReviewByItemId(itemID, page, accessToken)
        reviews.postValue(handleAllItemsResponse(response))
    }
    fun getReviewByUserId(page: Int ,accessToken: String)  = viewModelScope.launch {
        reviewByUserId.postValue(Resource.Loading())
        val response = reviewRepository.getReviewByUserId(page, accessToken)
        reviewByUserId.postValue(handleAllItemsResponse(response))
    }
    fun postReview(itemID: Int, rating: Int, content: String, accessToken: String) = viewModelScope.launch {
        val reviewPostBody = ReviewPostBody(itemID, rating, content)
        reviewPost.postValue(Resource.Loading())
        val response = reviewRepository.reviewPost(reviewPostBody, accessToken)
        reviewPost.postValue(handleReviewResponse(response))

    }
    fun updateReview(commentId: Int, itemID: Int, rating: Int, content: String, accessToken: String) = viewModelScope.launch {
        val updateReviewBody = UpdateReviewBody(commentId, itemID, rating, content)
        updateReview.postValue(Resource.Loading())
        val response = reviewRepository.updateReview(updateReviewBody, accessToken)
        updateReview.postValue(handleUpdateReviewResponse(response))

    }

    }
    private fun handleAllItemsResponse(response: Response<ReviewResponse>): Resource<ReviewResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleReviewResponse(response: Response<ReviewPostResponse>): Resource<ReviewPostResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleUpdateReviewResponse(response: Response<UpdateReviewResponse>): Resource<UpdateReviewResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




