package com.era.photosearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.era.photosearch.data.remote.ApiService
import com.era.photosearch.model.response.PhotoInfo
import com.era.photosearch.model.response.PhotoInfoResponse
import com.era.photosearch.model.response.toPhotos

class PhotoPagingSource(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, PhotoInfo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoInfo> {
        val page = params.key ?: 1
        return try {
            val response = apiService.search(query, page, params.loadSize)
            LoadResult.Page(
                data = (response.photos as List<PhotoInfoResponse?>?).toPhotos(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.photos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotoInfo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}