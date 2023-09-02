package com.example.imageshering.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imageshering.domain.PhotoInCollection

@Entity(tableName = "photo_in_collection")
class PhotoInCollectionDao (
    @PrimaryKey
    override val id: String,
    @ColumnInfo(name = "image_url")
    override val imageUrl: String,
    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String,
    override val name: String,
    override val username: String,
    @ColumnInfo(name = "liked_by_user")
    override var likedByUser: Boolean,
    override var likes: Int,
    @ColumnInfo(name = "download_event_url")
    override val downloadEventUrl: String,
    @ColumnInfo(name = "collection_id")
    val collectionId: String,
) : PhotoInCollection {
    constructor(photoInCollection: PhotoInCollection, collectionId: String) : this(
        id = photoInCollection.id,
        imageUrl = photoInCollection.imageUrl,
        avatarUrl = photoInCollection.avatarUrl,
        name = photoInCollection.name,
        username = photoInCollection.username,
        likedByUser = photoInCollection.likedByUser,
        likes = photoInCollection.likes,
        downloadEventUrl = photoInCollection.downloadEventUrl,
        collectionId = collectionId,
    )

}