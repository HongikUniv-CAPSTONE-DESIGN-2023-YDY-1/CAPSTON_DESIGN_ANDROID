package com.example.capstonedesign.data.reviewViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.response.ItemResponse
import com.example.capstonedesign.data.response.ReviewResponse
import com.example.capstonedesign.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ReviewViewModel(
    val reviewRepository: Repository
): ViewModel() {

    val reviews : MutableLiveData<Resource<ReviewResponse>> = MutableLiveData()



    fun getReviewByItemId(itemID: Int, page: Int ,accessToken: String)  = viewModelScope.launch {
        reviews.postValue(Resource.Loading())
        val response = reviewRepository.getReviewByItemId(itemID, page, accessToken)
        reviews.postValue(handleAllItemsResponse(response))
    }
    private fun handleAllItemsResponse(response: Response<ReviewResponse>): Resource<ReviewResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}