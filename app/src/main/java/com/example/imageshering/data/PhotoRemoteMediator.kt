package com.example.imageshering.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.imageshering.data.api.UnsplashApi
import com.example.imageshering.data.dao.CacheDao
import com.example.imageshering.data.dao.PhotoDao
import javax.inject.Inject

@ExperimentalPagingApi
class PhotoRemoteMediator @Inject constructor(
    private val cacheDao: CacheDao,
    private val unsplashApi: UnsplashApi,
    private val photosStateRepository: PhotosStateRepository
) : RemoteMediator<Int, PhotoDao>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoDao>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
                return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val photos = fetchPhotos(pageIndex)
            photosStateRepository.apiAvailable = true
            if (loadType == LoadType.REFRESH) {
                cacheDao.refresh(photos)
            } else {
                cacheDao.save(photos)
            }
            MediatorResult.Success(
                endOfPaginationReached = photos.size < UnsplashApi.UNSPLASH_PAGE_SIZE
            )
        } catch (e: Exception) {
            photosStateRepository.apiAvailable = false
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
//            LoadType.PREPEND -> if (pageIndex>1) --pageIndex else return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    private suspend fun fetchPhotos(
        pageIndex: Int
    ): List<PhotoDao> {
        return unsplashApi.photos(
            page = pageIndex
        )
        .map { PhotoDao(it) }
    }


}