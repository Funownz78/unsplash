package com.example.imageshering.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imageshering.domain.Photo

@Entity(tableName = "photos")
data class PhotoDao(
    @PrimaryKey
    override val id: String,
    @ColumnInfo(name = "create_at")
    override val createdAt: String,
    override val urlThumb: String,
    override val username: String,
    override val name: String,
    @ColumnInfo(name = "profile_image")
    override val profileImage: String,
    override val likes: Int,
    @ColumnInfo(name = "liked_by_user")
    override val likedByUser: Boolean,
) : Photo {
    constructor(photo: Photo) : this(
        id = photo.id,
        createdAt = photo.createdAt,
        urlThumb = photo.urlThumb,
        username = photo.username,
        name = photo.name,
        profileImage = photo.profileImage,
        likes = photo.likes,
        likedByUser = photo.likedByUser
    )
}