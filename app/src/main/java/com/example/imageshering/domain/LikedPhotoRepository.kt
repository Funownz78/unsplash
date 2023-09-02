package com.example.imageshering.domain

import com.example.imageshering.data.dto.liked.LikedPhotoDto

interface LikedPhotoRepository {
    suspend fun getLikedPhoto(username: String): List<LikedPhotoDto>
}