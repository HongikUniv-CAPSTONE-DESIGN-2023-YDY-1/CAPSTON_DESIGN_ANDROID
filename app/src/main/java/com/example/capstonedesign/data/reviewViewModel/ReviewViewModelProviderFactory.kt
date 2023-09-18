package com.example.capstonedesign.data.reviewViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstonedesign.data.itemViewModel.ItemSearchViewModel
import com.example.capstonedesign.data.repository.Repository

class ReviewViewModelProviderFactory(
    private val reviewRepository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReviewViewModel(reviewRepository) as T
    }
}