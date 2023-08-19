package com.example.capstonedesign.model

import android.content.ClipData.Item
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.capstonedesign.response.ItemResponse
import com.example.capstonedesign.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class ItemSearchViewModel(
    val itemRepository: Repository
) : ViewModel(){



    val allItems: MutableLiveData<Resource<ItemResponse>> = MutableLiveData()


    init{
        getAllItems()

    }

    fun getAllItems() = viewModelScope.launch {
        allItems.postValue(Resource.Loading())
        val response = itemRepository.getAllItems("", "STRONG")
        allItems.postValue(handleAllItemsResponse(response))
    }

    private fun handleAllItemsResponse(response: Response<ItemResponse>): Resource<ItemResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



}


