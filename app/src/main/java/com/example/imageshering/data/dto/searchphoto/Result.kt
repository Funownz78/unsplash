package com.example.imageshering.data.dto.searchphoto


import com.example.imageshering.domain.Photo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
//    @Json(name = "alt_description")
//    var altDescription: String = "",
//    @Json(name = "blur_hash")
//    var blurHash: String = "",
//    @Json(name = "color")
//    var color: String = "",
    @Json(name = "created_at")
    override var createdAt: String = "",
//    @Json(name = "current_user_collections")
//    var currentUserCollections: List<Any> = listOf(),
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
//    @Json(name = "links")
//    var links: Links = Links(),
//    @Json(name = "promoted_at")
//    var promotedAt: String? = "",
//    @Json(name = "sponsorship")
//    var sponsorship: Any? = Any(),
//    @Json(name = "tags")
//    var tags: List<Tag> = listOf(),
//    @Json(name = "topic_submissions")
//    var topicSubmissions: TopicSubmissionsX? = TopicSubmissionsX(),
//    @Json(name = "updated_at")
//    var updatedAt: String = "",
    @Json(name = "urls")
    var urls: Urls = Urls(),
    @Json(name = "user")
    var user: User = User(),
//    @Json(name = "width")
//    var width: Int = 0
) : Photo {
    override val urlThumb: String get() = urls.thumb
    override val username: String get() = user.username
    override val name: String get() = user.name
    override val profileImage: String get() = user.profileImage.small
}