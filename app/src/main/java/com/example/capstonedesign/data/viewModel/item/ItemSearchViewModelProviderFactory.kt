package com.example.capstonedesign.data.viewModel.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstonedesign.data.repository.Repository

class ItemSearchViewModelProviderFactory(
    private val itemRepository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemSearchViewModel(itemRepository) as T
    }
}