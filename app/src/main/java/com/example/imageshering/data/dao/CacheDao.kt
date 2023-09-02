package com.example.imageshering.data.dao

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface CacheDao {
    @Query("SELECT * FROM photos")
    fun getPagingSource(): PagingSource<Int, PhotoDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(photos: List<PhotoDao>)

    @Query("DELETE FROM photos")
    suspend fun clear()

    @Transaction
    suspend fun refresh(photos: List<PhotoDao>) {
        clear()
        save(photos)
    }

    suspend fun save(photo: PhotoDao) {
        save(listOf(photo))
    }

    @Query("SELECT * FROM photo_in_collection WHERE collection_id = :collectionId")
    fun getPagingSourcePhotoInCollection(collectionId: String): PagingSource<Int, PhotoInCollectionDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePhotoInCollection(photoInCollection: List<PhotoInCollectionDao>)

    @Query("DELETE FROM photo_in_collection WHERE collection_id = :collectionId")
    suspend fun clearPhotoInCollection(collectionId: String)

    @Transaction
    suspend fun refreshPhotoInCollection(
        photoInCollection: List<PhotoInCollectionDao>,
        collectionId: String,
    ) {
        clearPhotoInCollection(collectionId)
        savePhotoInCollection(photoInCollection)
    }

    suspend fun savePhotoInCollection(photoInCollection: PhotoInCollectionDao) {
        savePhotoInCollection(listOf(photoInCollection))
    }

    @Query("UPDATE photos SET liked_by_user = :likeStatus WHERE id = :id")
    suspend fun setLikeStatus(id: String, likeStatus: Boolean)

    @Query("SELECT * FROM collections")
    fun getCollectionsPagingSource(): PagingSource<Int, PhotoCollectionDao>

    @Query("SELECT * FROM photo_in_collection WHERE collection_id = :collectionId")
    fun getPhotoInCollectionsPagingSource(collectionId: String): PagingSource<Int, PhotoInCollectionDao>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCollection(collections: List<PhotoCollectionDao>)

    @Query("DELETE FROM collections")
    suspend fun clearCollection()

    @Transaction
    suspend fun refreshCollection(collections: List<PhotoCollectionDao>) {
        clearCollection()
        saveCollection(collections)
    }

    suspend fun saveCollection(collections: PhotoCollectionDao) {
        saveCollection(listOf(collections))
    }

    @Query("SELECT * FROM collection_details WHERE id=:id")
    fun getCollection(id: String): CollectionDetailsDao

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCollectionDetails(collections: List<CollectionDetailsDao>)

    suspend fun saveCollectionDetails(collection: CollectionDetailsDao) {
        saveCollectionDetails(listOf(collection))
    }
}