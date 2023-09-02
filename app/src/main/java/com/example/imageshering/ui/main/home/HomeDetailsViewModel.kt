package com.example.imageshering.ui.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageshering.data.PhotosStateRepository
import com.example.imageshering.domain.PhotoDetailed
import com.example.imageshering.domain.PhotoDetailedRepository
import com.example.imageshering.domain.PhotoLikeRepository
import com.example.imageshering.domain.StartDownloadUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "HomeDetailsViewModel"
class HomeDetailsViewModel @Inject constructor(
    private val photoDetailedRepository: PhotoDetailedRepository,
    photosStateRepository: PhotosStateRepository,
    private val photoLikeRepository: PhotoLikeRepository,
    private val startDownloadUseCase: StartDownloadUseCase,
) : ViewModel() {
    private val _photo = MutableStateFlow<PhotoDetailed?>(null)
    val photo = _photo.asStateFlow()
    val photosState = photosStateRepository.isApiAvailable
    private val _loadingState = MutableStateFlow(LoadingState.NotLoading)
    val loadingState = _loadingState.asStateFlow()
    private val _ioExceptionChannel = Channel<String>()
    val ioExceptionChannel = _ioExceptionChannel.receiveAsFlow()

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

    fun getPhoto(id: String) {
        viewModelScope.launch {
            _loadingState.value = LoadingState.Loading
            try {
                _photo.value = photoDetailedRepository.getPhoto(id)
            } catch (e: HttpException) {
                Log.d("imageshering", "ProfileViewModel, init: $e")
                _ioExceptionChannel.send("Limit request in hour exceeded")
            } catch (e: Exception) {
                Log.d(TAG, "getPhoto: $e")
            }
            _loadingState.value = LoadingState.NotLoading
        }
    }

    fun startDownload(downloadEventUrl: String) {
        viewModelScope.launch {
            startDownloadUseCase.execute(downloadEventUrl)
        }
    }

    enum class LoadingState {
        Loading, NotLoading
    }
}