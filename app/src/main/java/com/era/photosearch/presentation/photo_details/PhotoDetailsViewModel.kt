package com.era.photosearch.presentation.photo_details

import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor() : BaseViewModel<PhotoDetailsEvent>() {

}

sealed class PhotoDetailsEvent : BaseEvent() {

}