package com.era.photosearch.domain.repository

import androidx.paging.PagingData
import com.era.photosearch.model.response.PhotoInfo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun search(
        query: String,
        perPage: Int
    ): Flow<PagingData<PhotoInfo>>
}