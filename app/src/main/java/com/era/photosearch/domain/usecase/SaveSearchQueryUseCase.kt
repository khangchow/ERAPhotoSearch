package com.era.photosearch.domain.usecase

import com.era.photosearch.base.Result
import com.era.photosearch.base.getResult
import com.era.photosearch.di.AppModule.IODispatcher
import com.era.photosearch.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveSearchQueryUseCase @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String): Result<Any> {
        return withContext(dispatcher) {
            getResult {
                searchRepository.saveSearchQuery(query)
            }
        }
    }
}