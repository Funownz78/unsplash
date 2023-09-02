package com.example.imageshering.domain

interface Photo {
    val id: String
    val createdAt: String
    val urlThumb: String
    val username: String
    val name: String
    val profileImage: String
    val likes: Int
    val likedByUser: Boolean
}