package com.era.photosearch.presentation.select_photo_size

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.era.photosearch.base.BaseBottomSheetDialogFragment
import com.era.photosearch.databinding.BottomSheetSelectPhotoSizeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectPhotoSizeBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<SelectPhotoSizeEvent, BottomSheetSelectPhotoSizeBinding, SelectPhotoSizeViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> BottomSheetSelectPhotoSizeBinding) =
        BottomSheetSelectPhotoSizeBinding::inflate
    override val viewModel: SelectPhotoSizeViewModel by viewModels()

    companion object {
        const val SELECT_SIZE_REQUEST_KEY = "SELECT_SIZE_REQUEST_KEY"
        const val SIZE_BUNDLE_KEY = "SIZE_BUNDLE_KEY"
    }

    override suspend fun eventObserver() {

    }

    override fun bindComponent() {
        setUpRecyclerView()
        binding.ivClose.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setUpRecyclerView() {
        binding.rvSize.apply {
            adapter = PhotoSizeAdapter {
                setFragmentResult(SELECT_SIZE_REQUEST_KEY, bundleOf(SIZE_BUNDLE_KEY to it.name))
            }
            layoutManager = LinearLayoutManager(requireContext())
            viewModel.sizes.observe(viewLifecycleOwner) {
                (adapter as PhotoSizeAdapter).updateData(it)
            }
        }
    }

    override fun setResultListener() {

    }
}