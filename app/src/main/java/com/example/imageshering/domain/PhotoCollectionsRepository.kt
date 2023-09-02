package com.example.imageshering.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface PhotoCollectionsRepository {
    fun getPhotoCollectionsResult(query: String? = null): Flow<PagingData<PhotoCollection>>
}