package com.example.imageshering.data.dto.photoInCollection


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Urls(
//    @Json(name = "full")
//    var full: String = "",
//    @Json(name = "raw")
//    var raw: String = "",
    @Json(name = "regular")
    var regular: String = "",
//    @Json(name = "small")
//    var small: String = "",
//    @Json(name = "thumb")
//    var thumb: String = ""
)