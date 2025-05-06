package com.era.photosearch.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.domain.usecase.SearchPhotoUseCase
import com.era.photosearch.model.response.PhotoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    state: SavedStateHandle,
    searchPhotoUseCase: SearchPhotoUseCase
) : BaseViewModel<HomeEvent>() {
    val searchQuery = state.getLiveData("searchQuery", "")
    val photos: LiveData<PagingData<PhotoInfo>> = searchQuery.switchMap { query ->
        searchPhotoUseCase(query, 10)
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }
}

sealed class HomeEvent : BaseEvent() {

}