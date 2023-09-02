package com.example.imageshering.data.dto.searchphoto


import com.example.imageshering.domain.Photo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchPhotosDto(
    @Json(name = "results")
    var results: List<Result> = listOf(),
    @Json(name = "total")
    var total: Int = 0,
    @Json(name = "total_pages")
    var totalPages: Int = 0
)