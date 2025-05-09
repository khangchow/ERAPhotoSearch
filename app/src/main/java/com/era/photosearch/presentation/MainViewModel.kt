package com.era.photosearch.presentation

import androidx.lifecycle.viewModelScope
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.domain.usecase.SaveSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase
) : BaseViewModel<MainEvent>() {

    fun saveSearchQuery(searchQuery: String) {
        viewModelScope.launch {
            saveSearchQueryUseCase(searchQuery)
        }
    }

    fun onException(e: Exception?) {
        sendEvent(MainEvent.HandleException(e))
    }
}

sealed class MainEvent : BaseEvent() {
    data class HandleException(val e: Exception?) : MainEvent()
}