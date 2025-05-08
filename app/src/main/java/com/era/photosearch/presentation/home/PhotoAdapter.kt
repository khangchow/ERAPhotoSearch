package com.era.photosearch.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.era.photosearch.databinding.ItemPhotoBinding
import com.era.photosearch.model.response.PhotoInfo
import com.era.photosearch.presentation.home.PhotoAdapter.PhotoViewHolder

class PhotoAdapter(
    private val onPhotoClicked: (AppCompatImageView, PhotoInfo, String) -> Unit
) : PagingDataAdapter<PhotoInfo, PhotoViewHolder>(PHOTO_COMPARATOR) {
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) holder.bind(photo, onPhotoClicked)
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

        fun bind(
            photo: PhotoInfo,
            onPhotoClicked: (AppCompatImageView, PhotoInfo, String) -> Unit
        ) {
            binding.apply {
                ivPhoto.transitionName = StringBuilder(photo.id.toString()).toString()
                root.setOnClickListener { onPhotoClicked(ivPhoto, photo, ivPhoto.transitionName) }
                Glide.with(root.context).load(photo.src.small).into(ivPhoto)
            }
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