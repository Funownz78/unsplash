package com.example.imageshering.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.imageshering.domain.CollectionDetails

@Entity(tableName = "collection_details")
data class CollectionDetailsDao(
    @PrimaryKey
    override val id: String,
    override val title: String,
    @ColumnInfo(name = "collection_tags")
    override val collectionTags: String,
    override val description: String,
    @ColumnInfo(name = "total_photos")
    override val totalPhotos: Int,
    override val username: String,
    override val coverUrl: String,
) : CollectionDetails {
    constructor(collectionDetails: CollectionDetails) : this(
        id = collectionDetails.id,
        title = collectionDetails.title,
        collectionTags = collectionDetails.collectionTags,
        description = collectionDetails.description ?: "",
        totalPhotos = collectionDetails.totalPhotos,
        username = collectionDetails.username,
        coverUrl = collectionDetails.coverUrl,
    )
}
