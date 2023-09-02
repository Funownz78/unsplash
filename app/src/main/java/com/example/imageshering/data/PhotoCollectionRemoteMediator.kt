package com.example.imageshering.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.imageshering.data.api.UnsplashApi
import com.example.imageshering.data.dao.CacheDao
import com.example.imageshering.data.dao.PhotoCollectionDao
import javax.inject.Inject

@ExperimentalPagingApi
class PhotoCollectionRemoteMediator @Inject constructor(
    private val cacheDao: CacheDao,
    private val unsplashApi: UnsplashApi,
    private val photosStateRepository: PhotosStateRepository
) : RemoteMediator<Int, PhotoCollectionDao>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoCollectionDao>
    ): MediatorResult {

        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val photoCollections = fetchPhotos(pageIndex)
            photosStateRepository.apiAvailable = true
            if (loadType == LoadType.REFRESH) {
                cacheDao.refreshCollection(photoCollections)
            } else {
                cacheDao.saveCollection(photoCollections)
            }
            MediatorResult.Success(
                endOfPaginationReached = photoCollections.size < UnsplashApi.UNSPLASH_PAGE_SIZE
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
    ): List<PhotoCollectionDao> {
        return unsplashApi.photoCollections(
            page = pageIndex
        ).map { PhotoCollectionDao(it) }
    }


}