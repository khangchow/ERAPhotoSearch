package com.era.photosearch.presentation.home

import androidx.lifecycle.SavedStateHandle
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    state: SavedStateHandle
) : BaseViewModel<HomeEvent>() {

}

sealed class HomeEvent : BaseEvent() {

}