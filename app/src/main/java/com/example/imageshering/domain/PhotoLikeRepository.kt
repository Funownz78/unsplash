package com.example.imageshering.domain

interface PhotoLikeRepository {
    suspend fun setLike(id: String): Boolean
    suspend fun removeLike(id: String): Boolean
}