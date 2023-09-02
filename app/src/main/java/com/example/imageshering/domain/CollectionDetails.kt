package com.example.imageshering.domain

interface CollectionDetails {
    val id: String
    val title: String
    val totalPhotos: Int
    val coverUrl: String
    val collectionTags: String
    val description: String?
    val username: String
}