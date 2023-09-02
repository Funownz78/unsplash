package com.example.imageshering.data

import android.util.Log
import androidx.paging.*
import com.example.imageshering.data.api.UnsplashApi
import com.example.imageshering.data.api.UnsplashApi.Companion.BASE_URL
import com.example.imageshering.data.dao.CacheDao
import com.example.imageshering.data.dto.searchphoto.SearchPhotosDto
import com.example.imageshering.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UnsplashRepository @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val cacheDao: CacheDao,
    private val photosStateRepository: PhotosStateRepository
) :
    PhotosRepository,
    PhotoLikeRepository,
    PhotoDetailedRepository,
    SearchPhotoRepository,
    MeRepository,
    LikedPhotoRepository,
    PhotoCollectionsRepository,
    CollectionDetailsRepository,
    PhotoInCollectionRepository,
    DownloadEventRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotosResult(query: String?): Flow<PagingData<Photo>> =

        Pager(
            config = PagingConfig(
                pageSize = UnsplashApi.UNSPLASH_PAGE_SIZE,
                maxSize = UnsplashApi.UNSPLASH_PAGE_SIZE * 5,
                enablePlaceholders = false
            ),
            remoteMediator = PhotoRemoteMediator(cacheDao, unsplashApi, photosStateRepository),
            pagingSourceFactory = { cacheDao.getPagingSource()/*.also { pagingSource = it }*/ }
        ).flow.map { it as PagingData<Photo> }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotoCollectionsResult(query: String?): Flow<PagingData<PhotoCollection>> =

        Pager(
            config = PagingConfig(
                pageSize = UnsplashApi.UNSPLASH_PAGE_SIZE,
                maxSize = UnsplashApi.UNSPLASH_PAGE_SIZE * 5,
                enablePlaceholders = false
            ),
            remoteMediator = PhotoCollectionRemoteMediator(
                cacheDao,
                unsplashApi,
                photosStateRepository
            ),
            pagingSourceFactory = { cacheDao.getCollectionsPagingSource()/*.also { collectionsPagingSource = it } */ }
        ).flow.map { it as PagingData<PhotoCollection> }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPhotoInCollectionsResult(collectionId: String): Flow<PagingData<PhotoInCollection>> =

        Pager(
            config = PagingConfig(
                pageSize = UnsplashApi.UNSPLASH_PAGE_SIZE,
                maxSize = UnsplashApi.UNSPLASH_PAGE_SIZE * 5,
                enablePlaceholders = false
            ),
            remoteMediator = PhotoInCollectionRemoteMediator(
                collectionId,
                cacheDao,
                unsplashApi,
                photosStateRepository
            ),
            pagingSourceFactory = { cacheDao.getPhotoInCollectionsPagingSource(collectionId)/*.also { collectionsPagingSource = it } */ }
        ).flow.map { it as PagingData<PhotoInCollection> }

    override suspend fun getSearchedPhotos(
        query: String,
        page: Int,
        perPage: Int
    ): SearchPhotosDto =
        unsplashApi.searchPhotos(query, page, perPage)

    override suspend fun getLikedPhoto(username: String) = unsplashApi.likedPhoto(username)

    override suspend fun setLike(id: String): Boolean {
        val response = unsplashApi.setLike(id)
        val photo = response.body()?.photo
        return if (
            response.isSuccessful &&
            photo != null &&
            photo.likedByUser
        ) {
            cacheDao.setLikeStatus(id, true)
            true
        } else
            false

    }

    override suspend fun removeLike(id: String): Boolean {
        val response = unsplashApi.removeLike(id)
        val photo = response.body()?.photo
        return if (
            response.isSuccessful &&
            photo != null &&
            !photo.likedByUser
        ) {
            cacheDao.setLikeStatus(id, false)
            true
        } else
            false
    }

    override suspend fun getPhoto(id: String): PhotoDetailed = unsplashApi.photo(id)

    override suspend fun getMe(): Self = unsplashApi.me()

    override suspend fun getCollectionDetails(id: String): CollectionDetails {
        return unsplashApi.collectionDetails(id)
    }

    override suspend fun startDownload(endpoint: String) {
        try {
            unsplashApi.simpleGet(endpoint.removePrefix(BASE_URL))
        } catch (e: Exception) {
            Log.d("TAG", "startDownload: $e")
        }
    }
}