package com.era.photosearch.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.domain.usecase.SearchPhotoUseCase
import com.era.photosearch.model.response.PhotoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    searchPhotoUseCase: SearchPhotoUseCase
) : BaseViewModel<MainEvent>() {
    private val photosFlow: Flow<PagingData<PhotoInfo>> =
        searchPhotoUseCase("nature", 10).cachedIn(viewModelScope)
    val photos = photosFlow.asLiveData()
}

sealed class MainEvent : BaseEvent() {

}