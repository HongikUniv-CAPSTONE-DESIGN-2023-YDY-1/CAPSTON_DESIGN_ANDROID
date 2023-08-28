package com.example.capstonedesign.itemViewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstonedesign.repository.Repository
import com.example.capstonedesign.response.ItemResponse
import com.example.capstonedesign.response.ResponseData
import com.example.capstonedesign.response.SearchItem
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

    fun uploadImgToServer(imgUrl: String) = viewModelScope.launch {

        val file = File(imgUrl)
        val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val request = MultipartBody.Part.createFormData("img", file.name, imageRequestBody)
        Items.postValue(Resource.Loading())
        val response = itemRepository.uploadImgToServer(request)
        response.body()?.let { resultResponse ->
            Log.d("ItemSearchViewModel", "uploadImgToServer: $resultResponse")

        }
        Items.postValue(Resource.Success(response.body()!!))
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






