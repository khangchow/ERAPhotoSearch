package com.era.photosearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.era.photosearch.data.local.SearchQueryDao
import com.era.photosearch.domain.repository.SearchRepository
import com.era.photosearch.model.entity.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val searchQueryDao: SearchQueryDao
) : SearchRepository {
    override suspend fun saveSearchQuery(searchQuery: String) {
        searchQueryDao.upsertSearch(searchQuery)
    }

    override fun getSearchQueries(
        searchQuery: String,
        perPage: Int
    ): Flow<PagingData<SearchQueryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = perPage,
                initialLoadSize = perPage,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { searchQueryDao.getSearchQueries(searchQuery) }
        ).flow
    }
}