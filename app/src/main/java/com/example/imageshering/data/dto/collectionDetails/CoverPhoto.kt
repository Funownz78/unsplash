package com.example.imageshering.data.dto.collectionDetails


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverPhoto(
//    @Json(name = "alt_description")
//    var altDescription: String = "",
//    @Json(name = "blur_hash")
//    var blurHash: String = "",
//    @Json(name = "color")
//    var color: String = "",
//    @Json(name = "created_at")
//    var createdAt: String = "",
//    @Json(name = "current_user_collections")
//    var currentUserCollections: List<Any> = listOf(),
//    @Json(name = "description")
//    var description: Any? = Any(),
//    @Json(name = "height")
//    var height: Int = 0,
//    @Json(name = "id")
//    var id: String = "",
//    @Json(name = "liked_by_user")
//    var likedByUser: Boolean = false,
//    @Json(name = "likes")
//    var likes: Int = 0,
//    @Json(name = "links")
//    var links: Links = Links(),
//    @Json(name = "promoted_at")
//    var promotedAt: Any? = Any(),
//    @Json(name = "sponsorship")
//    var sponsorship: Any? = Any(),
//    @Json(name = "topic_submissions")
//    var topicSubmissions: TopicSubmissions = TopicSubmissions(),
//    @Json(name = "updated_at")
//    var updatedAt: String = "",
    @Json(name = "urls")
    var urls: Urls = Urls(),
//    @Json(name = "user")
//    var user: User = User(),
//    @Json(name = "width")
//    var width: Int = 0
)