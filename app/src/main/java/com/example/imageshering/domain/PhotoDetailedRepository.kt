package com.example.imageshering.domain

interface PhotoDetailedRepository {
    suspend fun getPhoto(id: String): PhotoDetailed
}