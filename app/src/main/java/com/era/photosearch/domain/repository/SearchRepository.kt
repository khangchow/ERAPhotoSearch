package com.era.photosearch.domain.repository

import androidx.paging.PagingData
import com.era.photosearch.model.entity.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun saveSearchQuery(searchQuery: String)
    fun getSearchQueries(searchQuery: String, perPage: Int): Flow<PagingData<SearchQueryEntity>>
    suspend fun deleteByQuery(searchQuery: String)
}