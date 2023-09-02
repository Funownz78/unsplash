package com.example.imageshering.data.dto.searchphoto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
//    @Json(name = "accepted_tos")
//    var acceptedTos: Boolean = false,
//    @Json(name = "bio")
//    var bio: Any? = Any(),
//    @Json(name = "first_name")
//    var firstName: String = "",
//    @Json(name = "for_hire")
//    var forHire: Boolean = false,
//    @Json(name = "id")
//    var id: String = "",
//    @Json(name = "instagram_username")
//    var instagramUsername: String? = "",
//    @Json(name = "last_name")
//    var lastName: String = "",
//    @Json(name = "links")
//    var links: LinksXX = LinksXX(),
//    @Json(name = "location")
//    var location: String? = "",
    @Json(name = "name")
    var name: String = "",
//    @Json(name = "portfolio_url")
//    var portfolioUrl: Any? = Any(),
    @Json(name = "profile_image")
    var profileImage: ProfileImage = ProfileImage(),
//    @Json(name = "social")
//    var social: Social = Social(),
//    @Json(name = "total_collections")
//    var totalCollections: Int = 0,
//    @Json(name = "total_likes")
//    var totalLikes: Int = 0,
//    @Json(name = "total_photos")
//    var totalPhotos: Int = 0,
//    @Json(name = "twitter_username")
//    var twitterUsername: Any? = Any(),
//    @Json(name = "updated_at")
//    var updatedAt: String = "",
    @Json(name = "username")
    var username: String = ""
)