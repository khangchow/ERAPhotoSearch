package com.era.photosearch.model.ui

import com.era.photosearch.util.PhotoSize

data class PhotoSizeUiModel(
    val size: PhotoSize,
    val isSelected: Boolean = false
)