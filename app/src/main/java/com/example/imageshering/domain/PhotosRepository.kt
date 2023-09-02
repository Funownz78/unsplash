package com.example.imageshering.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow


interface PhotosRepository {
    fun getPhotosResult(query: String? = null): Flow<PagingData<Photo>>
}