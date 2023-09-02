package com.example.imageshering.ui.main.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class CollectionViewModelFactory @Inject constructor(
    private val collectionsViewModel: CollectionsViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = collectionsViewModel as T
}