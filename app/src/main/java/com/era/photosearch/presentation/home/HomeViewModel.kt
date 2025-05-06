package com.era.photosearch.presentation.home

import androidx.lifecycle.SavedStateHandle
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BaseViewModel<HomeEvent>() {
    var searchQuery = state["searchQuery"] ?: ""
        set(value) {
            field = value
            state["searchQuery"] = value
        }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }
}

sealed class HomeEvent : BaseEvent() {

}