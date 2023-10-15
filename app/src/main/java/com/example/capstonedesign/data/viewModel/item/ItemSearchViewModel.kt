package com.example.capstonedesign.data.viewModel.item


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.response.ItemResponse
import com.example.capstonedesign.data.response.RecommendData
import com.example.capstonedesign.data.response.RecommendListResponse

import com.example.capstonedesign.data.response.ResponseData
import com.example.capstonedesign.data.response.SearchItem
import com.example.capstonedesign.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File


class ItemSearchViewModel(
    val itemRepository: Repository
) : ViewModel() {


    val Items: MutableLiveData<Resource<ItemResponse>> = MutableLiveData()

    val CamerItems: MutableLiveData<Resource<ItemResponse>> = MutableLiveData()

    val RecommendItems: MutableLiveData<Resource<RecommendListResponse>> = MutableLiveData()



    init {
        getAllItems()
    }

    fun getAllItems() = viewModelScope.launch {
        Items.postValue(Resource.Loading())
        val response = itemRepository.getAllItems("", "STRONG")
        Items.postValue(handleAllItemsResponse(response))
    }

    fun searchItems(name: String, strength: String) = viewModelScope.launch {
        Items.postValue(Resource.Loading())
        val response = itemRepository.getAllItems(name, strength)
        Items.postValue(handleAllItemsResponse(response))
    }

    fun searchPromotion(promotion: String) = viewModelScope.launch {
        Items.postValue(Resource.Loading())

        val response = itemRepository.getAllItems("", "STRONG")
        val list = response.body()?.response?.searchItems
        val list2 = mutableListOf<SearchItem>()
        for (i in list!!) {
            if (i.promotion == promotion) {
                list2.add(i)
            }
        }
        val itemResponse = ItemResponse("success", ResponseData(list2))
        Items.postValue(Resource.Success(itemResponse))
    }
    fun searchBrand(brand: String) = viewModelScope.launch {
        Items.postValue(Resource.Loading())

        val response = itemRepository.getAllItems("", "STRONG")
        val list = response.body()?.response?.searchItems
        val list3 = mutableListOf<SearchItem>()
        for (i in list!!) {
            if (i.brand == brand) {
                list3.add(i)
            }
        }
        val itemResponse = ItemResponse("success", ResponseData(list3))
        Items.postValue(Resource.Success(itemResponse))
    }

    fun uploadImgToServer(imgUrl: String) = viewModelScope.launch {

        val file = File(imgUrl)
        val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val request = MultipartBody.Part.createFormData("img", file.name, imageRequestBody)
        CamerItems.postValue(Resource.Loading())
        val response = itemRepository.uploadImgToServer(request)
        response.body()?.let { resultResponse ->
            Log.d("ItemSearchViewModel", "uploadImgToServer: $resultResponse")

        }
        CamerItems.postValue(handleAllItemsResponse(response))
    }

    private fun handleAllItemsResponse(response: Response<ItemResponse>): Resource<ItemResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getRecommendList(accessToken: String) = viewModelScope.launch {
        RecommendItems.postValue(Resource.Loading())
        val response = itemRepository.getRecommendList(accessToken)
        RecommendItems.postValue(handleRecommendListResponse(response))
    }

    private fun handleRecommendListResponse(response: Response<RecommendListResponse>): Resource<RecommendListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message().toString())
    }


}






