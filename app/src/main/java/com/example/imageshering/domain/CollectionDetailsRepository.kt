package com.example.imageshering.domain

interface CollectionDetailsRepository {
    suspend fun getCollectionDetails(id: String): CollectionDetails
}