package com.example.imageshering.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imageshering.domain.PhotoCollection

@Entity(tableName = "collections")
data class PhotoCollectionDao(
    @PrimaryKey
    override val id: String,
    @ColumnInfo(name = "total_photos")
    override val totalPhotos: Int,
    override val title: String,
    @ColumnInfo(name = "image_url")
    override val imageUrl: String,
    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String,
    override val name: String,
    override val username: String,
) : PhotoCollection {
    constructor(photoCollection: PhotoCollection) : this(
        id = photoCollection.id,
        totalPhotos = photoCollection.totalPhotos,
        title = photoCollection.title,
        imageUrl = photoCollection.imageUrl,
        avatarUrl = photoCollection.avatarUrl,
        name = photoCollection.name,
        username = photoCollection.username,
    )
}