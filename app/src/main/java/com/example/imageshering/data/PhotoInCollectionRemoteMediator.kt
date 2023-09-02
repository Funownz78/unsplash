package com.example.imageshering.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.imageshering.data.api.UnsplashApi
import com.example.imageshering.data.dao.CacheDao
import com.example.imageshering.data.dao.PhotoInCollectionDao
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val TAG = "CollectionDetailsFragme"
@ExperimentalPagingApi
class PhotoInCollectionRemoteMediator @Inject constructor(
    private val collectionId: String,
    private val cacheDao: CacheDao,
    private val unsplashApi: UnsplashApi,
    private val photosStateRepository: PhotosStateRepository
) : RemoteMediator<Int, PhotoInCollectionDao>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoInCollectionDao>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
                return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            Log.d(TAG, "load: ")
            val photoInCollectionDao = fetchPhotosFake(pageIndex)
            Log.d(TAG, "load: ${photoInCollectionDao.size}")
            photosStateRepository.apiAvailable = true
            if (loadType == LoadType.REFRESH) {
                cacheDao.refreshPhotoInCollection(photoInCollectionDao, collectionId)
            } else {
                cacheDao.savePhotoInCollection(photoInCollectionDao)
            }
            MediatorResult.Success(
                endOfPaginationReached = photoInCollectionDao.size < UnsplashApi.UNSPLASH_PAGE_SIZE
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
    ): List<PhotoInCollectionDao> {
        return unsplashApi.photoInCollection(
            collectionId = collectionId,
            page = pageIndex,
        )
        .map { PhotoInCollectionDao(it, collectionId) }
    }

    private suspend fun fetchPhotosFake(
        pageIndex: Int
    ): List<PhotoInCollectionDao> {
        delay(10)
        return when(pageIndex) {
            0 -> listOf(
                PhotoInCollectionDao(
                    id = "gKXKBY-C-Dk",
                    avatarUrl = "https://images.unsplash.com/profile-fb-1514888261-0e72294039e0.jpg?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
                    imageUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=Mnw0MTY1ODR8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjgxNzUwOTAx&ixlib=rb-4.0.3&q=80&w=1080",
                    name = "Fake Name",
                    username = "fake_username",
                    downloadEventUrl = "https://api.unsplash.com/photos/gKXKBY-C-Dk/download?ixid=Mnw0MTY1ODR8MHwxfHNlYXJjaHwxfHxjYXR8ZW58MHx8fHwxNjgxNzUwOTAx",
                    likes = 560,
                    likedByUser = false,
                    collectionId = collectionId,
                ),
                PhotoInCollectionDao(
                    id = "75715CVEJhI",
                    avatarUrl = "https://images.unsplash.com/profile-1622042628346-724d32229f67image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
                    imageUrl = "https://images.unsplash.com/photo-1573865526739-10659fec78a5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=Mnw0MTY1ODR8MHwxfHNlYXJjaHwyfHxjYXR8ZW58MHx8fHwxNjgxNzUwOTAx&ixlib=rb-4.0.3&q=80&w=1080",
                    name = "Fake Name",
                    username = "fake_username",
                    downloadEventUrl = "https://api.unsplash.com/photos/75715CVEJhI/download?ixid=Mnw0MTY1ODR8MHwxfHNlYXJjaHwyfHxjYXR8ZW58MHx8fHwxNjgxNzUwOTAx",
                    likes = 560,
                    likedByUser = false,
                    collectionId = collectionId,
                ),
                PhotoInCollectionDao(
                    id = "mJaD10XeD7w",
                    avatarUrl = "https://images.unsplash.com/profile-1658213151083-e0cf976f3395image?ixlib=rb-4.0.3&crop=faces&fit=crop&w=32&h=32",
                    imageUrl = "https://images.unsplash.com/photo-1495360010541-f48722b34f7d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=Mnw0MTY1ODR8MHwxfHNlYXJjaHwzfHxjYXR8ZW58MHx8fHwxNjgxNzUwOTAx&ixlib=rb-4.0.3&q=80&w=1080",
                    name = "Fake Name",
                    username = "fake_username",
                    downloadEventUrl = "https://api.unsplash.com/photos/mJaD10XeD7w/download?ixid=Mnw0MTY1ODR8MHwxfHNlYXJjaHwzfHxjYXR8ZW58MHx8fHwxNjgxNzUwOTAx",
                    likes = 560,
                    likedByUser = false,
                    collectionId = collectionId,
                ),
            )
            else -> emptyList()
        }
    }
}