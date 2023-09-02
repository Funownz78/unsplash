package com.example.imageshering.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface PhotoInCollectionRepository {
    fun getPhotoInCollectionsResult(collectionId: String): Flow<PagingData<PhotoInCollection>>
}