package com.example.imageshering.data.dto.collections


import com.example.imageshering.domain.PhotoCollection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionDto(
    @Json(name = "cover_photo")
    var coverPhoto: CoverPhoto = CoverPhoto(),
//    @Json(name = "curated")
//    var curated: Boolean = false,
    @Json(name = "description")
    var description: String? = "",
//    @Json(name = "featured")
//    var featured: Boolean = false,
    @Json(name = "id")
    override var id: String = "",
//    @Json(name = "last_collected_at")
//    var lastCollectedAt: String = "",
//    @Json(name = "links")
//    var links: LinksXX = LinksXX(),
//    @Json(name = "preview_photos")
//    var previewPhotos: List<PreviewPhoto> = listOf(),
//    @Json(name = "private")
//    var `private`: Boolean = false,
//    @Json(name = "published_at")
//    var publishedAt: String = "",
//    @Json(name = "share_key")
//    var shareKey: String = "",
//    @Json(name = "tags")
//    var tags: List<Tag> = listOf(),
    @Json(name = "title")
    override var title: String = "",
    @Json(name = "total_photos")
    override var totalPhotos: Int = 0,
//    @Json(name = "updated_at")
//    var updatedAt: String = "",
//    @Json(name = "user")
//    var user: UserXX = UserXX()
) : PhotoCollection {
    override val imageUrl: String get() = coverPhoto.urls.regular
    override val avatarUrl: String get() = coverPhoto.user.profileImage.small
    override val name: String get() = coverPhoto.user.name
    override val username: String get() = coverPhoto.user.username
}