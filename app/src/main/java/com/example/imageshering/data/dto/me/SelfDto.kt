package com.example.imageshering.data.dto.me


import com.example.imageshering.domain.Self
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SelfDto(
//    @Json(name = "accepted_tos")
//    var acceptedTos: Boolean = false,
//    @Json(name = "allow_messages")
//    var allowMessages: Boolean = false,
//    @Json(name = "badge")
//    var badge: Any? = Any(),
    @Json(name = "bio")
    override var bio: String? = null,
//    @Json(name = "confirmed")
//    var confirmed: Boolean = false,
//    @Json(name = "dmca_verification")
//    var dmcaVerification: String = "",
    @Json(name = "downloads")
    override var downloads: Int = 0,
    @Json(name = "email")
    override var email: String? = null,
//    @Json(name = "first_name")
//    var firstName: String = "",
//    @Json(name = "followed_by_user")
//    var followedByUser: Boolean = false,
//    @Json(name = "followers_count")
//    var followersCount: Int = 0,
//    @Json(name = "following_count")
//    var followingCount: Int = 0,
//    @Json(name = "for_hire")
//    var forHire: Boolean = false,
    @Json(name = "id")
    override var id: String = "",
//    @Json(name = "instagram_username")
//    var instagramUsername: String = "",
//    @Json(name = "last_name")
//    var lastName: String = "",
//    @Json(name = "links")
//    var links: Links = Links(),
    @Json(name = "location")
    override var location: String? = null,
//    @Json(name = "meta")
//    var meta: Meta = Meta(),
    @Json(name = "name")
    override var name: String = "",
//    @Json(name = "numeric_id")
//    var numericId: Int = 0,
//    @Json(name = "photos")
//    var photos: List<Photo> = listOf(),
//    @Json(name = "portfolio_url")
//    var portfolioUrl: Any? = Any(),
    @Json(name = "profile_image")
    var profileImage: ProfileImage = ProfileImage(),
//    @Json(name = "social")
//    var social: Social = Social(),
//    @Json(name = "tags")
//    var tags: Tags = Tags(),
//    @Json(name = "total_collections")
//    var totalCollections: Int = 0,
//    @Json(name = "total_likes")
//    var totalLikes: Int = 0,
//    @Json(name = "total_photos")
//    override var totalPhotos: Int = 0,
//    @Json(name = "twitter_username")
//    var twitterUsername: String = "",
//    @Json(name = "uid")
//    var uid: String = "",
//    @Json(name = "unlimited_uploads")
//    var unlimitedUploads: Boolean = false,
//    @Json(name = "unread_highlight_notifications")
//    var unreadHighlightNotifications: Boolean = false,
//    @Json(name = "unread_in_app_notifications")
//    var unreadInAppNotifications: Boolean = false,
//    @Json(name = "updated_at")
//    var updatedAt: String = "",
//    @Json(name = "uploads_remaining")
//    var uploadsRemaining: Int = 0,
    @Json(name = "username")
    override var username: String = ""
) : Self {
    override val profileImageUrl: String get() = profileImage.medium
}