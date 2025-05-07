package com.era.photosearch.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.era.photosearch.domain.repository.SearchRepository
import com.era.photosearch.model.entity.SearchQuery
import com.era.photosearch.model.entity.toSearchQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchQueriesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(
        query: String,
        perPage: Int
    ): Flow<PagingData<SearchQuery>> {
        return searchRepository.getSearchQueries(query, perPage)
            .map { it.map { it.toSearchQuery() } }
    }
}