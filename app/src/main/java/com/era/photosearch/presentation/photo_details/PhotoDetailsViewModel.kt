package com.era.photosearch.presentation.photo_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.util.PhotoSize
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BaseViewModel<PhotoDetailsEvent>() {

    companion object {
        const val PHOTO_SIZE_KEY = "PHOTO_SIZE_KEY"
    }

    private val _photoSize = state.getLiveData<PhotoSize>(PHOTO_SIZE_KEY, PhotoSize.SMALL)
    val photoSize: LiveData<PhotoSize> = _photoSize

    fun updateSize(photoSize: PhotoSize) {
        _photoSize.value = photoSize
        state[PHOTO_SIZE_KEY] = photoSize
    }
}

sealed class PhotoDetailsEvent : BaseEvent() {

}