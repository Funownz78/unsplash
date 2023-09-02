package com.example.imageshering.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class SearchPhotoViewModelFactory @Inject constructor(
    private val searchPhotoViewModel: SearchPhotoViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = searchPhotoViewModel as T
}