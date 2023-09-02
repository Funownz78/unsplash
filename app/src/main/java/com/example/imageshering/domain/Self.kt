package com.example.imageshering.domain

interface Self {
    val id: String
    val name: String
    val username: String
    val profileImageUrl: String
    val bio: String?
    val location: String?
    val email: String?
    val downloads: Int
}