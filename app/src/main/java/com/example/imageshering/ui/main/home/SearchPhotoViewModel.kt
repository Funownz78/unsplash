package com.example.imageshering.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageshering.data.api.UnsplashApi
import com.example.imageshering.data.photos.SearchPhotosPagingSource
import com.example.imageshering.domain.Photo
import com.example.imageshering.domain.PhotoLikeRepository
import com.example.imageshering.domain.SearchPhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchPhotoViewModel"
class SearchPhotoViewModel @Inject constructor(
    private val photoLikeRepository: PhotoLikeRepository,
    private val searchPhotoRepository: SearchPhotoRepository,
) : ViewModel() {
    lateinit var searchedPhotos: Flow<PagingData<Photo>>

    fun setPagingDataWithQuery(query: String) {
        searchedPhotos = setSearchedPhoto(query)
    }

    fun isInitialized(): Boolean = ::searchedPhotos.isInitialized

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

    private fun setSearchedPhoto(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(UnsplashApi.UNSPLASH_PAGE_SIZE),
            pagingSourceFactory = { SearchPhotosPagingSource(query, searchPhotoRepository) }
        ).flow.cachedIn(viewModelScope)
    }
}