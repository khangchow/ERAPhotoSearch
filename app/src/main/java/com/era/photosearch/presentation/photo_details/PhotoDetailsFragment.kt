package com.era.photosearch.presentation.photo_details

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentPhotoDetailsBinding
import com.era.photosearch.extension.customSpan
import com.era.photosearch.extension.navigate
import com.era.photosearch.util.PhotoSize
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailsFragment :
    BaseFragment<PhotoDetailsEvent, FragmentPhotoDetailsBinding, PhotoDetailsViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoDetailsBinding) =
        FragmentPhotoDetailsBinding::inflate
    override val viewModel: PhotoDetailsViewModel by viewModels()
    private val args: PhotoDetailsFragmentArgs by navArgs()

    override suspend fun eventObserver() {

    }

    override fun bindComponent() {
        setUpPhoto()
        setUpPhotographerName()
        binding.apply {
            root.setOnClickListener { llInfo.isInvisible = !llInfo.isInvisible }
            tvOriginalResolution.text =
                getString(
                    R.string.photo_original_resolution,
//                    viewModel.photoInfo?.width,
//                    viewModel.photoInfo?.height
                    args.photoInfo.width,
                    args.photoInfo.height
                )
        }
    }

    private fun setUpPhoto() {
        binding.ivPhoto.apply {
            postponeEnterTransition()
            transitionName = viewModel.transitionName
            val enterTransition = TransitionInflater.from(requireContext()).inflateTransition(
                android.R.transition.move
            )
            val returnTransition = MaterialContainerTransform().apply {
                drawingViewId = R.id.nav_host_fragment
                scrimColor = Color.TRANSPARENT
            }
            sharedElementEnterTransition = enterTransition
            sharedElementReturnTransition = returnTransition
            viewModel.photoSize.observe(viewLifecycleOwner) {
                val url: String?
                val size: String
                when (it) {
                    PhotoSize.LANDSCAPE -> {
                        url = viewModel.photoInfo?.src?.landscape
                        size = getString(R.string.landscape)
                    }

                    PhotoSize.LARGE -> {
                        url = viewModel.photoInfo?.src?.large
                        size = getString(R.string.large)
                    }

                    PhotoSize.EXTRA_LARGE -> {
                        url = viewModel.photoInfo?.src?.large2x
                        size = getString(R.string.extra_large)
                    }

                    PhotoSize.MEDIUM -> {
                        url = viewModel.photoInfo?.src?.medium
                        size = getString(R.string.medium)
                    }

                    PhotoSize.ORIGINAL -> {
                        url = viewModel.photoInfo?.src?.original
                        size = getString(R.string.original)
                    }

                    PhotoSize.PORTRAIT -> {
                        url = viewModel.photoInfo?.src?.portrait
                        size = getString(R.string.portrait)
                    }

                    PhotoSize.SMALL -> {
                        url = viewModel.photoInfo?.src?.small
                        size = getString(R.string.small)
                    }

                    PhotoSize.TINY -> {
                        url = viewModel.photoInfo?.src?.tiny
                        size = getString(R.string.tiny)
                    }
                }
                setUpPhotoSize(size)
                try {
                    Glide.with(requireContext()).load(url).dontTransform()
                        .listener(
                            object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable?>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    startPostponedEnterTransition()
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    model: Any,
                                    target: Target<Drawable?>?,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    startPostponedEnterTransition()
                                    return false
                                }
                            }
                        ).into(this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setUpPhotoSize(size: String) {
        val original = getString(R.string.photo_current_size, size)
        val start = original.indexOf(size)
        binding.tvCurrentSize.apply {
            text = SpannableString(original).customSpan(
                context = requireContext(),
                colorId = R.color.onPrimary,
                styleId = Typeface.BOLD,
                startPosition = start,
                endPosition = start + size.length,
                isUnderLine = true,
                onClickSpan = {
//                    navigate(
//                        directions = PhotoDetailsFragmentDirections.actionPhotoDetailsFragmentToSelectPhotoSizeBottomSheetDialogFragment(),
//                        rootFragment = this@PhotoDetailsFragment
//                    )
                })
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            isClickable = true
        }
    }

    private fun setUpPhotographerName() {
        val original = getString(R.string.photographer_name, viewModel.photoInfo?.photographer)
        val start = original.indexOf(viewModel.photoInfo?.photographer.orEmpty())
        binding.tvPhotographer.apply {
            text = SpannableString(original).customSpan(
                context = requireContext(),
                colorId = R.color.onPrimary,
                styleId = Typeface.BOLD,
                startPosition = start,
                endPosition = start + (viewModel.photoInfo?.photographer?.length ?: 0),
                isUnderLine = true,
                onClickSpan = {
                    navigate(
                        directions = PhotoDetailsFragmentDirections.actionGlobalWebViewFragment(
                            viewModel.photoInfo?.photographerUrl.orEmpty()
                        ), rootFragment = this@PhotoDetailsFragment
                    )
                })
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            isClickable = true
        }
    }

    override fun setResultListener() {

    }
}