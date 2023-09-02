package com.example.imageshering.data.dto.detailedphoto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
//    @Json(name = "source")
//    var source: Source? = Source(),
    @Json(name = "title")
    var title: String = "",
    @Json(name = "type")
    var type: String = ""
)