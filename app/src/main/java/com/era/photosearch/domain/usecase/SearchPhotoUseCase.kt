package com.era.photosearch.domain.usecase

import androidx.paging.PagingData
import com.era.photosearch.domain.repository.PhotoRepository
import com.era.photosearch.model.response.PhotoInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    operator fun invoke(
        query: String,
        perPage: Int
    ): Flow<PagingData<PhotoInfo>> {
        return photoRepository.search(query, perPage)
    }
}