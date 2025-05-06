package com.era.photosearch.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.era.photosearch.databinding.ItemPhotoBinding
import com.era.photosearch.model.response.PhotoInfo
import com.era.photosearch.presentation.PhotoAdapter.PhotoViewHolder

class PhotoAdapter : PagingDataAdapter<PhotoInfo, PhotoViewHolder>(PHOTO_COMPARATOR) {
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) holder.bind(photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoInfo) {
            Glide.with(binding.root.context).load(photo.src.original).into(binding.ivPhoto)
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<PhotoInfo>() {
            override fun areItemsTheSame(oldItem: PhotoInfo, newItem: PhotoInfo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotoInfo, newItem: PhotoInfo) =
                oldItem == newItem
        }
    }
}
