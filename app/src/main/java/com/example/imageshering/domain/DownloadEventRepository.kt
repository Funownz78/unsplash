package com.example.imageshering.domain

interface DownloadEventRepository {
    suspend fun startDownload(endpoint: String)
}