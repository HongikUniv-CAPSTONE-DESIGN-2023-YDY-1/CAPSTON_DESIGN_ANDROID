package com.example.capstonedesign.itemViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.capstonedesign.repository.Repository

class ItemSearchViewModelProviderFactory(
    private val itemRepository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemSearchViewModel(itemRepository) as T
    }
}