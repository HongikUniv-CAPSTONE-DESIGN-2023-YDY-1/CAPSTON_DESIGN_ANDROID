package com.example.capstonedesign.data.viewModel.reviews

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
    private val reviewRepository: Repository
) : ViewModel() {

    val reviews: MutableLiveData<Resource<ReviewResponse>> = MutableLiveData()
    val reviewPost: MutableLiveData<Resource<ReviewPostResponse>> = MutableLiveData()
    val reviewByUserId: MutableLiveData<Resource<ReviewResponse>> = MutableLiveData()
    val updateReview: MutableLiveData<Resource<UpdateReviewResponse>> = MutableLiveData()


    fun getReviewByItemId(itemID: Int, page: Int, accessToken: String) = viewModelScope.launch {
        reviews.postValue(Resource.Loading())
        val response = reviewRepository.getReviewByItemId(itemID, page, accessToken)
        reviews.postValue(handleAllItemsResponse(response))
    }

    fun getReviewByUserId(page: Int, accessToken: String) = viewModelScope.launch {
        reviewByUserId.postValue(Resource.Loading())
        val response = reviewRepository.getReviewByUserId(page, accessToken)
        reviewByUserId.postValue(handleAllItemsResponse(response))
    }

    fun postReview(itemID: Int, rating: Int, content: String, accessToken: String) =
        viewModelScope.launch {
            val reviewPostBody = ReviewPostBody(itemID, rating, content)
            reviewPost.value = Resource.Loading()

            try {
                val response = reviewRepository.reviewPost(reviewPostBody, accessToken)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        reviewPost.value = Resource.Success(resultResponse)
                    }
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorMessage = "Error Message from Server: $errorResponse"
                    reviewPost.value = Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                // Retrofit에서 발생하는 예외를 처리
                val errorMessage = "Network Request Failed: ${e.message}"
                reviewPost.value = Resource.Error(errorMessage)
            }
        }

    fun updateReview(
        commentId: Int,
        itemID: Int,
        rating: Int,
        content: String,
        accessToken: String
    ) = viewModelScope.launch {
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
    try {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    } catch (e: Exception) {
        return Resource.Error(e.message ?: "An error occurred")
    }
}


private fun handleUpdateReviewResponse(response: Response<UpdateReviewResponse>): Resource<UpdateReviewResponse> {
    if (response.isSuccessful) {
        response.body()?.let { resultResponse ->
            return Resource.Success(resultResponse)
        }
    }
    return Resource.Error(response.message())
}




