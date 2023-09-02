package com.example.imageshering.domain

import com.example.imageshering.data.dto.searchphoto.SearchPhotosDto

interface SearchPhotoRepository{
    suspend fun getSearchedPhotos(query: String, page: Int, perPage: Int): SearchPhotosDto
}