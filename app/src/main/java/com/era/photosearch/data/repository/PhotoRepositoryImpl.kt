package com.era.photosearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.era.photosearch.data.paging.PhotoPagingSource
import com.era.photosearch.data.remote.ApiService
import com.era.photosearch.domain.repository.PhotoRepository
import com.era.photosearch.model.response.PhotoInfo
import kotlinx.coroutines.flow.Flow

class PhotoRepositoryImpl(private val apiService: ApiService) : PhotoRepository {
    override fun search(
        query: String,
        perPage: Int
    ): Flow<PagingData<PhotoInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = perPage,
                initialLoadSize = perPage,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource(apiService, query) }
        ).flow
    }
}