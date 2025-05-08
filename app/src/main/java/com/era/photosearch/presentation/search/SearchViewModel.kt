package com.era.photosearch.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.domain.usecase.DeleteSearchQueryUseCase
import com.era.photosearch.domain.usecase.GetSearchQueriesUseCase
import com.era.photosearch.model.entity.SearchQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    state: SavedStateHandle,
    getSearchQueriesUseCase: GetSearchQueriesUseCase,
    private val deleteSearchQueryUseCase: DeleteSearchQueryUseCase
) : BaseViewModel<SearchEvent>() {
    private val _searchQuery = state.getLiveData("searchQuery", "")
    val searchQuery: LiveData<String> = _searchQuery
    val histories: LiveData<PagingData<SearchQuery>> = searchQuery.switchMap { query ->
        getSearchQueriesUseCase(query, 30)
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    fun onQueryTextChanged(query: String) {
        _searchQuery.value = query
    }

    fun deleteSearchQuery(query: String) {
        viewModelScope.launch {
            deleteSearchQueryUseCase(query).execute {
                sendEvent(SearchEvent.DeletedSearchQuery(query))
            }
        }
    }
}

sealed class SearchEvent : BaseEvent() {
    data class DeletedSearchQuery(val query: String) : SearchEvent()
}