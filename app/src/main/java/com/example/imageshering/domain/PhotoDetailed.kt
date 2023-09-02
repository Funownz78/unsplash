package com.example.imageshering.domain

interface PhotoDetailed {
    val id: String
//    val urlThumb: String
    val urlRegular: String
    val urlRaw: String
    val username: String
    val name: String
    val profileImage: String
    val likes: Int
    val likedByUser: Boolean
    val locationName: String
    val locationLatitude: Double?
    val locationLongitude: Double?
    val imageTags: String
    val exifName: String
    val exifModel: String
    val exifExposure: String
    val exifAperture: String
    val exifLength: String
    val exifIso: String
    val about: String
    val downloads: Int
    val downloadEventUrl: String
}
