package com.era.photosearch.presentation.select_photo_size

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.viewbinding.ViewBinding
import com.era.photosearch.R
import com.era.photosearch.base.BaseRecyclerViewAdapter
import com.era.photosearch.databinding.ItemPhotoSizeBinding
import com.era.photosearch.model.ui.PhotoSizeUiModel
import com.era.photosearch.util.PhotoSize

class PhotoSizeAdapter(
    private val onItemClicked: (PhotoSize) -> Unit
) : BaseRecyclerViewAdapter<PhotoSizeUiModel, ItemPhotoSizeBinding>(
    checkItems = { old, new ->
        old.size == new.size
    },
    checkContents = { old, new ->
        old == new
    }
) {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemPhotoSizeBinding =
        ItemPhotoSizeBinding::inflate

    override fun bindViewHolder(item: PhotoSizeUiModel, binding: ViewBinding, viewType: Int) {
        (binding as ItemPhotoSizeBinding).apply {
            tvSize.text = when (item.size) {
                PhotoSize.LANDSCAPE -> root.context.getString(R.string.landscape)
                PhotoSize.LARGE -> root.context.getString(R.string.large)
                PhotoSize.EXTRA_LARGE -> root.context.getString(R.string.extra_large)
                PhotoSize.MEDIUM -> root.context.getString(R.string.medium)
                PhotoSize.ORIGINAL -> root.context.getString(R.string.original)
                PhotoSize.PORTRAIT -> root.context.getString(R.string.portrait)
                PhotoSize.SMALL -> root.context.getString(R.string.small)
                PhotoSize.TINY -> root.context.getString(R.string.tiny)
            }
            root.setOnClickListener {
                onItemClicked.invoke(item.size)
            }
            ivSelected.isInvisible = !item.isSelected
        }
    }
}