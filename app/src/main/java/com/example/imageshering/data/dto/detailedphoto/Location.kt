package com.example.imageshering.data.dto.detailedphoto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
//    @Json(name = "city")
//    var city: Any? = Any(),
//    @Json(name = "country")
//    var country: Any? = Any(),
    @Json(name = "name")
    var name: Any? = Any(),
    @Json(name = "position")
    var position: Position = Position()
)