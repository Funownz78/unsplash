package com.example.imageshering.data.dto.me


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileImage(
//    @Json(name = "large")
//    var large: String = "",
    @Json(name = "medium")
    var medium: String = "",
//    @Json(name = "small")
//    var small: String = ""
)