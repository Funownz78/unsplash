package com.example.imageshering.data.dto.detailedphoto


import com.example.imageshering.domain.PhotoDetailed
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoDetailsDto(
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
    @Json(name = "downloads")
    override var downloads: Int = 0,
    @Json(name = "exif")
    var exif: Exif = Exif(),
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
    @Json(name = "location")
    var location: Location = Location(),
//    @Json(name = "meta")
//    var meta: Meta = Meta(),
//    @Json(name = "promoted_at")
//    var promotedAt: Any? = Any(),
//    @Json(name = "public_domain")
//    var publicDomain: Boolean = false,
//    @Json(name = "related_collections")
//    var relatedCollections: RelatedCollections = RelatedCollections(),
//    @Json(name = "sponsorship")
//    var sponsorship: Sponsorship = Sponsorship(),
    @Json(name = "tags")
    var tags: List<Tag> = listOf(),
//    @Json(name = "tags_preview")
//    var tagsPreview: List<TagsPreview> = listOf(),
//    @Json(name = "topic_submissions")
//    var topicSubmissions: TopicSubmissionsXXXX = TopicSubmissionsXXXX(),
//    @Json(name = "topics")
//    var topics: List<Any> = listOf(),
//    @Json(name = "updated_at")
//    var updatedAt: String = "",
    @Json(name = "urls")
    var urls: Urls = Urls(),
    @Json(name = "user")
    var user: User = User(),
//    @Json(name = "views")
//    var views: Int = 0,
//    @Json(name = "width")
//    var width: Int = 0
) : PhotoDetailed {
    override val urlRegular: String get() = urls.regular
    override val urlRaw: String get() = urls.raw
//    override val urlThumb: String get() = urls.thumb
    override val username: String get() = user.username
    override val name: String get() = user.name
    override val profileImage: String get() = user.profileImage.small
    override val locationName: String get() = location.name?.toString() ?: ""
    override val locationLatitude: Double? get() = location.position.latitude
    override val locationLongitude: Double? get() = location.position.longitude
    override val exifName: String get() = exif.name ?: ""
    override val exifModel: String get() = exif.model ?: ""
    override val exifExposure: String get() = exif.exposureTime ?: ""
    override val exifAperture: String get() = exif.aperture ?: ""
    override val exifLength: String get() = exif.focalLength ?: ""
    override val exifIso: String get() = exif.iso?.toString() ?: ""
    override val about: String get() = user.bio ?: ""
    override val downloadEventUrl: String get() = links.downloadLocation
    override val imageTags: String get() = tags
        .filter { it.type == "landing_page" }
        .joinToString(" ") { "#${it.title}" }
}