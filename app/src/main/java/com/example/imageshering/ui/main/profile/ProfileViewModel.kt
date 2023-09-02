package com.example.imageshering.ui.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageshering.domain.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "ProfileViewModel"
class ProfileViewModel @Inject constructor(
    private val meRepository: MeRepository,
    private val likedPhotoRepository: LikedPhotoRepository,
    private val photoLikeRepository: PhotoLikeRepository,
) : ViewModel() {
    private val _me = MutableStateFlow<Self?>(null)
    val me = _me.asStateFlow()

    private val _likedPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val likedPhotos = _likedPhotos.asStateFlow()

    private val _ioExceptionChannel = Channel<String>()
    val ioExceptionChannel = _ioExceptionChannel.receiveAsFlow()

    private val _loadingState = MutableStateFlow(LoadingState.NotLoading)
    val loadingState = _loadingState.asStateFlow()

    init {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            try {
                val meResponse = meRepository.getMe()
                _me.value = meResponse
            } catch (e: HttpException) {
                Log.d("imageshering", "ProfileViewModel, init: $e")
                _ioExceptionChannel.send("Limit request in hour exceeded")
            } catch (e: Exception) {
                Log.d(TAG, "init: $e")
            }
            _loadingState.value = LoadingState.NotLoading
        }
    }

    fun refreshLikedPhoto() {
        _loadingState.value = LoadingState.Loading
        viewModelScope.launch {
            try {
                reloadLikedPhoto()
            } catch (e: HttpException) {
                Log.d("imageshering", "ProfileViewModel, init: $e")
                _ioExceptionChannel.send("Limit request in hour exceeded")
            } catch (e: Exception) {
                Log.d(TAG, "init: $e")
            }
        }
        _loadingState.value = LoadingState.NotLoading
    }

    private suspend fun reloadLikedPhoto() {
        me.value?.let {
            _likedPhotos.value = likedPhotoRepository.getLikedPhoto(it.username)
        }
    }

    fun setLike(id: String, undoCallback: () -> Unit) {
        viewModelScope.launch {
            var result = false
            try {
                result = photoLikeRepository.setLike(id)
                reloadLikedPhoto()
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
                reloadLikedPhoto()
            } catch (e: Exception) {
                Log.d(TAG, "removeLike: $e")
            }
            if (!result)
                undoCallback()
        }
    }

    enum class LoadingState {
        Loading, NotLoading
    }
}