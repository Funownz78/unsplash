package com.example.imageshering.data.api

import com.example.imageshering.data.dto.collectionDetails.CollectionDetailsDto
import com.example.imageshering.data.dto.collections.CollectionDto
import com.example.imageshering.data.dto.detailedphoto.PhotoDetailsDto
import com.example.imageshering.data.dto.liked.LikedPhotoDto
import com.example.imageshering.data.dto.me.SelfDto
import com.example.imageshering.data.dto.photoInCollection.PhotoInCollectionDto
import com.example.imageshering.data.dto.photos.PhotosResponseDtoItem
import com.example.imageshering.data.dto.photos.SinglePhotoResponse
import com.example.imageshering.data.dto.searchphoto.SearchPhotosDto
import retrofit2.Response
import retrofit2.http.*

interface UnsplashApi {
    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val UNSPLASH_STARTING_PAGE_INDEX = 1
        const val UNSPLASH_PAGE_SIZE = 20
    }

    @Headers("Accept-Version: v1")
    @GET("{request}")
    suspend fun simpleGet(@Path("request") id: String)

    @Headers("Accept-Version: v1")
    @GET("photos")
    suspend fun photos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = UNSPLASH_PAGE_SIZE
    ): List<PhotosResponseDtoItem>

    @Headers("Accept-Version: v1")
    @GET("collections/{id}/photos")
    suspend fun photoInCollection(
        @Path("id") collectionId: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = UNSPLASH_PAGE_SIZE
    ): List<PhotoInCollectionDto>

    @Headers("Accept-Version: v1")
    @GET("collections")
    suspend fun photoCollections(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = UNSPLASH_PAGE_SIZE
    ): List<CollectionDto>

    @Headers("Accept-Version: v1")
    @GET("users/{username}/likes")
    suspend fun likedPhoto(
        @Path("username") username: String,
    ): List<LikedPhotoDto>

    @Headers("Accept-Version: v1")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String = "",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = UNSPLASH_PAGE_SIZE
    ): SearchPhotosDto

    @Headers("Accept-Version: v1")
    @GET("photos/{id}")
    suspend fun photo(@Path("id") id: String): PhotoDetailsDto

    @Headers("Accept-Version: v1")
    @GET("me")
    suspend fun me(): SelfDto

    @Headers("Accept-Version: v1")
    @POST("photos/{id}/like")
    suspend fun setLike(@Path("id") id: String): Response<SinglePhotoResponse>

    @Headers("Accept-Version: v1")
    @DELETE("photos/{id}/like")
    suspend fun removeLike(@Path("id") id: String): Response<SinglePhotoResponse>

    @Headers("Accept-Version: v1")
    @GET("collections/{id}")
    suspend fun collectionDetails(@Path("id") id: String): CollectionDetailsDto

}