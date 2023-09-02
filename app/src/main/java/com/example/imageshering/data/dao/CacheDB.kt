package com.example.imageshering.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.imageshering.data.dao.*


@Database(entities = [
    PhotoDao::class,
    PhotoCollectionDao::class,
    CollectionDetailsDao::class,
    PhotoInCollectionDao::class,
], version = 3)
abstract class CacheDb : RoomDatabase() {
    companion object {
        private var dbInstance: CacheDb? = null
        fun getCacheDbInstance(context: Context): CacheDb =
            dbInstance ?: Room.databaseBuilder(
                context = context.applicationContext,
                klass = CacheDb::class.java,
                name = "rda_230322"
            ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    abstract fun myDao(): CacheDao
}