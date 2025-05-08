package com.era.photosearch.presentation.select_photo_size

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.switchMap
import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import com.era.photosearch.domain.usecase.GetPhotoSizeUseCase
import com.era.photosearch.model.ui.PhotoSizeUiModel
import com.era.photosearch.util.PhotoSize
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectPhotoSizeViewModel @Inject constructor(
    state: SavedStateHandle,
    private val getPhotoSizeUseCase: GetPhotoSizeUseCase
) : BaseViewModel<SelectPhotoSizeEvent>() {

    companion object {
        const val SIZE_KEY = "size"
    }

    val selectedSize = state.getLiveData<PhotoSize>(SIZE_KEY)
    val sizes = selectedSize.switchMap {
        MutableLiveData<List<PhotoSizeUiModel>>(

        )
    }


}

sealed class SelectPhotoSizeEvent : BaseEvent() {

}