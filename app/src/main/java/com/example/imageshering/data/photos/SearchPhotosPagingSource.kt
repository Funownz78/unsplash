package com.example.imageshering.data.photos

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageshering.data.api.UnsplashApi.Companion.UNSPLASH_STARTING_PAGE_INDEX
import com.example.imageshering.domain.Photo
import com.example.imageshering.domain.SearchPhotoRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "RDA"

class SearchPhotosPagingSource @Inject constructor(
    private val query: String,
    private val searchPhotoRepository: SearchPhotoRepository,
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val response = searchPhotoRepository.getSearchedPhotos(
                query = query,
                page = position,
                perPage = params.loadSize
            )
            LoadResult.Page(
                data = response.results,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (response.results.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            Log.d(TAG, "load IOException: $exception")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d(TAG, "load HttpException: $exception")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int =
        UNSPLASH_STARTING_PAGE_INDEX

}