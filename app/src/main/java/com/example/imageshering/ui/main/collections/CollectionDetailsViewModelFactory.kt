package com.example.imageshering.ui.main.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class CollectionDetailsViewModelFactory @Inject constructor(
    private val collectionDetailsViewModel: CollectionDetailsViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = collectionDetailsViewModel as T
}