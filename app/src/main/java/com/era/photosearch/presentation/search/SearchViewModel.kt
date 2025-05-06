package com.era.photosearch.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    state: SavedStateHandle
) : BaseViewModel<SearchEvent>() {
    private val _searchQuery = state.getLiveData("searchQuery", "")
    val searchQuery: LiveData<String> = _searchQuery

    fun onQueryTextChanged(query: String) {
        _searchQuery.value = query
    }

}

sealed class SearchEvent : BaseEvent() {

}