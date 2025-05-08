package com.era.photosearch.presentation.select_photo_size

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.domain.usecase.GetPhotoSizeUseCase
import com.era.photosearch.model.ui.PhotoSizeUiModel
import com.era.photosearch.util.PhotoSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPhotoSizeViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPhotoSizeUseCase: GetPhotoSizeUseCase
) : BaseViewModel<SelectPhotoSizeEvent>() {
    private val _sizes = MutableLiveData<List<PhotoSizeUiModel>>(emptyList())
    val sizes: LiveData<List<PhotoSizeUiModel>> = _sizes

    init {
        viewModelScope.launch {
            getPhotoSizeUseCase(state.get<PhotoSize>("size")).execute {
                _sizes.value = it
            }
        }
    }

}

sealed class SelectPhotoSizeEvent : BaseEvent() {

}