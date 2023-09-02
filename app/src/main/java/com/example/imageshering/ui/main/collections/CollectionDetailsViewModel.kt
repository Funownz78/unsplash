package com.example.imageshering.ui.main.collections

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.imageshering.data.PhotosStateRepository
import com.example.imageshering.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CollectionDetailsViewMo"
class CollectionDetailsViewModel @Inject constructor(
    private val collectionDetailsRepository: CollectionDetailsRepository,
    private val photoInCollectionRepository: PhotoInCollectionRepository,
    photosStateRepository: PhotosStateRepository,
    private val photoLikeRepository: PhotoLikeRepository,
) : ViewModel() {
    private val _collectionDetails = MutableStateFlow<CollectionDetails?>(null)
    val collectionDetails = _collectionDetails.asStateFlow()

    lateinit var photoInCollection: Flow<PagingData<PhotoInCollection>>

    fun getCollectionDetails(id: String) {
        viewModelScope.launch {
            try {
                _collectionDetails.value = collectionDetailsRepository.getCollectionDetails(id)
            } catch (e: Exception) {
                Log.d(TAG, "getCollectionDetails: $e")
            }
        }
        viewModelScope.launch {
            photoInCollection = photoInCollectionRepository.getPhotoInCollectionsResult(id)
        }
    }

    fun isInitialized(): Boolean = ::photoInCollection.isInitialized

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