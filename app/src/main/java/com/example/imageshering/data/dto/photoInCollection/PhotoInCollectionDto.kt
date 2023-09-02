package com.example.imageshering.data.dto.photoInCollection


import com.example.imageshering.domain.PhotoInCollection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoInCollectionDto(
//    @Json(name = "blur_hash")
//    var blurHash: String = "",
//    @Json(name = "color")
//    var color: String = "",
//    @Json(name = "created_at")
//    var createdAt: String = "",
//    @Json(name = "current_user_collections")
//    var currentUserCollections: List<CurrentUserCollection> = listOf(),
//    @Json(name = "description")
//    var description: String = "",
//    @Json(name = "height")
//    var height: Int = 0,
    @Json(name = "id")
    override var id: String = "",
    @Json(name = "liked_by_user")
    override var likedByUser: Boolean = false,
    @Json(name = "likes")
    override var likes: Int = 0,
    @Json(name = "links")
    var links: Links = Links(),
//    @Json(name = "updated_at")
//    var updatedAt: String = "",
    @Json(name = "urls")
    var urls: Urls = Urls(),
    @Json(name = "user")
    var user: User = User(),
//    @Json(name = "width")
//    var width: Int = 0
) : PhotoInCollection {
    override val imageUrl: String get() = urls.regular
    override val avatarUrl: String get() = user.profileImage.small
    override val name: String get() = user.name
    override val username: String get() = user.username
    override val downloadEventUrl: String get() = links.downloadLocation
}