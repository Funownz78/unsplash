package com.example.imageshering.domain

interface PhotoInCollection {
    val id: String
    val imageUrl: String
    val avatarUrl: String
    val name: String
    val username: String
    var likedByUser: Boolean
    var likes: Int
    val downloadEventUrl: String
}