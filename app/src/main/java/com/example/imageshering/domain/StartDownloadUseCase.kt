package com.example.imageshering.domain

import javax.inject.Inject

class StartDownloadUseCase @Inject constructor(
    private val downloadEventRepository: DownloadEventRepository,
) {
    suspend fun execute(url: String)  {
        downloadEventRepository.startDownload(url)
    }
}
