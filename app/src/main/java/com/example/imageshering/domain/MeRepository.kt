package com.example.imageshering.domain

interface MeRepository {
    suspend fun getMe(): Self
}