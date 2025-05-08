package com.era.photosearch.presentation.select_photo_size

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.era.photosearch.base.BaseBottomSheetDialogFragment
import com.era.photosearch.databinding.BottomSheetSelectPhotoSizeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPhotoSizeBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<SelectPhotoSizeEvent, BottomSheetSelectPhotoSizeBinding, SelectPhotoSizeViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> BottomSheetSelectPhotoSizeBinding) =
        BottomSheetSelectPhotoSizeBinding::inflate
    override val viewModel: SelectPhotoSizeViewModel by viewModels()

    override suspend fun eventObserver() {

    }

    override fun bindComponent() {

    }

    private fun setUpRecyclerView() {

    }

    override fun setResultListener() {

    }
}