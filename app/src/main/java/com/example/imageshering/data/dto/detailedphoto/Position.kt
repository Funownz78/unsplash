package com.example.imageshering.data.dto.detailedphoto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Position(
    @Json(name = "latitude")
    var latitude: Double? = 0.0,
    @Json(name = "longitude")
    var longitude: Double? = -118.243685
)