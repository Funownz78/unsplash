package com.example.imageshering.di

import android.content.Context
import com.example.imageshering.data.dao.CacheDao
import com.example.imageshering.data.CacheDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
    @Provides
    @Singleton
    fun provideCacheDb(context: Context): CacheDb = CacheDb.getCacheDbInstance(context)

    @Provides
    @Singleton
    fun provideCacheDao(cacheDb: CacheDb): CacheDao = cacheDb.myDao()
}