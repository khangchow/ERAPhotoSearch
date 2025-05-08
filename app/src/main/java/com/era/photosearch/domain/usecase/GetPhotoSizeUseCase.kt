package com.era.photosearch.domain.usecase

import com.era.photosearch.base.Result
import com.era.photosearch.base.getResult
import com.era.photosearch.di.AppModule.IODispatcher
import com.era.photosearch.model.ui.PhotoSizeUiModel
import com.era.photosearch.util.PhotoSize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPhotoSizeUseCase @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(selectedSize: PhotoSize): Result<List<PhotoSizeUiModel>> {
        return withContext(dispatcher) {
            getResult {
                PhotoSize.entries.map {
                    PhotoSizeUiModel(size = it.name, isSelected = it == selectedSize)
                }.toList()
            }
        }
    }
}