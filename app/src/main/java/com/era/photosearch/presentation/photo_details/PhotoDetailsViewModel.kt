package com.era.photosearch.presentation.photo_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.model.response.PhotoInfo
import com.era.photosearch.util.PhotoSize
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    state: SavedStateHandle
) : BaseViewModel<PhotoDetailsEvent>() {

    companion object {
        const val PHOTO_INFO_JSON = "PHOTO_INFO_JSON"
    }

    val transitionName = state.get<String>("transitionName")
    val photoInfo = state.get<PhotoInfo>("photoInfo")
    private val _photoSize = MutableLiveData<PhotoSize>(PhotoSize.SMALL)
    val photoSize: LiveData<PhotoSize> = _photoSize


}

sealed class PhotoDetailsEvent : BaseEvent() {

}