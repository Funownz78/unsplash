package com.example.imageshering.di

import android.content.Context
import com.example.imageshering.data.AppAuthComponent
import com.example.imageshering.data.PhotosStateRepository
import com.example.imageshering.data.UnsplashRepository
import com.example.imageshering.data.api.UnsplashApi
import com.example.imageshering.data.dao.CacheDao
import com.example.imageshering.domain.*
import com.example.imageshering.ui.main.collections.CollectionDetailsViewModelFactory
import com.example.imageshering.ui.main.collections.CollectionViewModelFactory
import com.example.imageshering.ui.main.home.HomeDetailsViewModelFactory
import com.example.imageshering.ui.main.home.HomeListViewModelFactory
import com.example.imageshering.ui.main.home.SearchPhotoViewModelFactory
import com.example.imageshering.ui.main.profile.ProfileViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [DaggerAuthModule::class, ApiModule::class, HomeModule::class, AppModule::class, DbModule::class])
interface DaggerComponent {
    fun getAppAuthComponent(): AppAuthComponent
    fun getHomeViewModelFactory(): HomeListViewModelFactory
    fun getHomeDetailsViewModelFactory(): HomeDetailsViewModelFactory
    fun getSearchPhotoViewModelFactory(): SearchPhotoViewModelFactory
    fun getProfileViewModelFactory(): ProfileViewModelFactory
    fun getCollectionViewModelFactory(): CollectionViewModelFactory
    fun getCollectionDetailsViewModelFactory(): CollectionDetailsViewModelFactory
}

@Module
class HomeModule {
    @Provides
    @Singleton
    fun providePhotosRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): PhotosRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun provideDownloadEventRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): DownloadEventRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun providePhotoInCollectionRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): PhotoInCollectionRepository =
        UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun provideCollectionDetailsRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): CollectionDetailsRepository =
        UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun providePhotoCollectionsRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): PhotoCollectionsRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun provideLikedPhotoRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): LikedPhotoRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun provideMeRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): MeRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun provideSearchPhotoRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): SearchPhotoRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun providePhotoDetailedRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): PhotoDetailedRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)

    @Provides
    @Singleton
    fun providePhotosStateRepository() = PhotosStateRepository()

    @Provides
    @Singleton
    fun providePhotoLikeRepository(
        unsplashApi: UnsplashApi,
        cacheDao: CacheDao,
        photosStateRepository: PhotosStateRepository
    ): PhotoLikeRepository = UnsplashRepository(unsplashApi, cacheDao, photosStateRepository)
}

@Module
class DaggerAuthModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideAppAuthComponent() = AppAuthComponent(context)


}