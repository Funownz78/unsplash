package com.example.imageshering.domain

import javax.inject.Inject

class GetPhotoCollectionsUseCase @Inject constructor(
    private val photoCollectionsRepository: PhotoCollectionsRepository
) {
    fun getPhotoCollectionsResult() =
        photoCollectionsRepository.getPhotoCollectionsResult()
}