package com.example.imageshering.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.imageshering.data.PhotosStateRepository
import com.example.imageshering.domain.GetPhotosUseCase
import com.example.imageshering.domain.PhotoLikeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeListViewModel"
class HomeListViewModel @Inject constructor(
    getPhotosUseCase: GetPhotosUseCase,
    photosStateRepository: PhotosStateRepository,
    private val photoLikeRepository: PhotoLikeRepository,
) : ViewModel() {
    val pagedPhotos =
        getPhotosUseCase.getPhotosResult().cachedIn(viewModelScope)
    val photosState = photosStateRepository.isApiAvailable

    fun setLike(id: String, undoCallback: () -> Unit) {
        viewModelScope.launch {
            var result = false
            try {
                result = photoLikeRepository.setLike(id)
            } catch (e: Exception) {
                Log.d(TAG, "setLike: $e")
            }
            if (!result)
                undoCallback()
        }
    }

    fun removeLike(id: String, undoCallback: () -> Unit) {
        viewModelScope.launch {
            var result = false
            try {
                result = photoLikeRepository.removeLike(id)
            } catch (e: Exception) {
                Log.d(TAG, "removeLike: $e")
            }
            if (!result)
                undoCallback()
        }
    }

}