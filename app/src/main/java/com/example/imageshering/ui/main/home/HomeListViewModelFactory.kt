package com.example.imageshering.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class HomeListViewModelFactory @Inject constructor(
    private val homeViewModel: HomeListViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = homeViewModel as T
}