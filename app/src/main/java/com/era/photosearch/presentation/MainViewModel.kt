package com.era.photosearch.presentation

import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
//    searchPhotoUseCase: SearchPhotoUseCase
) : BaseViewModel<MainEvent>() {
//    private val photosFlow: Flow<PagingData<PhotoInfo>> =
//        searchPhotoUseCase("nature", 10).cachedIn(viewModelScope)
//    val photos = photosFlow.asLiveData()
}

sealed class MainEvent : BaseEvent() {

}