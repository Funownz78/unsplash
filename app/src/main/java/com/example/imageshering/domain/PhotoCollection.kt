package com.example.imageshering.domain

interface PhotoCollection {
    val id: String
    val title: String
    val totalPhotos: Int
    val imageUrl: String
    val avatarUrl: String
    val name: String
    val username: String
}
