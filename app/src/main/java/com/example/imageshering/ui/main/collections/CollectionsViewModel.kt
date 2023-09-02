package com.example.imageshering.ui.main.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.imageshering.data.PhotosStateRepository
import com.example.imageshering.domain.GetPhotoCollectionsUseCase
import javax.inject.Inject

class CollectionsViewModel @Inject constructor(
    getPhotoCollectionsUseCase: GetPhotoCollectionsUseCase,
    photosStateRepository: PhotosStateRepository
) : ViewModel() {
    val pagedPhotoCollections =
        getPhotoCollectionsUseCase.getPhotoCollectionsResult().cachedIn(viewModelScope)
    val photosState = photosStateRepository.isApiAvailable
}