package com.example.imageshering.data.dto.detailedphoto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Exif(
    @Json(name = "aperture")
    var aperture: String? = null,
    @Json(name = "exposure_time")
    var exposureTime: String? = null,
    @Json(name = "focal_length")
    var focalLength: String? = null,
    @Json(name = "iso")
    var iso: Int? = null,
    @Json(name = "make")
    var make: String? = null,
    @Json(name = "model")
    var model: String? = null,
    @Json(name = "name")
    var name: String? = null
)