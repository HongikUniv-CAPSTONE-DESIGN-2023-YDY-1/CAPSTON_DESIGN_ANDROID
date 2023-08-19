package com.example.capstonedesign.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ItemSearchViewModelProviderFactory(
    val itemRepository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemSearchViewModel(itemRepository) as T
    }
}