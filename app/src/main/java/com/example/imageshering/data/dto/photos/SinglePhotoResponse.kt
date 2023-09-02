package com.example.imageshering.data.dto.photos


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SinglePhotoResponse(
    @Json(name = "photo")
    var photo: PhotosResponseDtoItem? = null,
//    @Json(name = "user")
//    var user: UserXX = UserXX()
)