package com.example.imageshering.domain

import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(private val photosRepository: PhotosRepository) {
    fun getPhotosResult() = photosRepository.getPhotosResult()
}