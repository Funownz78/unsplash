package com.example.imageshering.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PhotosStateRepository @Inject constructor() {
    private val _isApiAvailable = MutableStateFlow(false)
    val isApiAvailable = _isApiAvailable.asStateFlow()
    var apiAvailable = false
        set(value) {
        _isApiAvailable.value = value
        field = value
    }
}