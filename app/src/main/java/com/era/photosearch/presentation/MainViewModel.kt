package com.era.photosearch.presentation

import androidx.lifecycle.viewModelScope
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainEvent>() {

    fun handleException(exception: Exception) {
        viewModelScope.launch {
            sendEvent(MainEvent.HandleException(exception))
        }
    }
}

sealed class MainEvent : BaseEvent() {
    data class HandleException(val exception: Exception) : MainEvent()
}