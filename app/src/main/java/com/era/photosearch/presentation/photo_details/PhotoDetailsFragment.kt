package com.era.photosearch.presentation.photo_details

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import com.era.photosearch.presentation.select_photo_size.SelectPhotoSizeBottomSheetDialogFragment
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
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private var isInfoExpanded = true

    override suspend fun eventObserver() {

    }

    override fun bindComponent() {
        setUpPhoto()
        setUpPhotographerName()
        setUpInfoSectionAnimation()
        binding.apply {
            tvOriginalResolution.text =
                getString(
                    R.string.photo_original_resolution,
                    args.photoInfo.width,
                    args.photoInfo.height
                )
        }
    }

    private fun setUpInfoSectionAnimation() {
        binding.apply {
            vInfo.setOnClickListener {
                if (isInfoExpanded) {
                    isInfoExpanded = false
                    ivArrow.setImageResource(R.drawable.ic_arrow_up)
                    motionLayout.transitionToEnd()
                } else {
                    isInfoExpanded = true
                    ivArrow.setImageResource(R.drawable.ic_arrow_down)
                    motionLayout.transitionToStart()
                }
            }
        }
    }

    private fun setUpPhoto() {
        binding.ivPhoto.apply {
            scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
            setOnTouchListener { _, event ->
                scaleGestureDetector.onTouchEvent(event)
                performClick()
                true
            }
            postponeEnterTransition()
            transitionName = args.transitionName
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
                        url = args.photoInfo.src.landscape
                        size = getString(R.string.landscape)
                    }

                    PhotoSize.LARGE -> {
                        url = args.photoInfo.src.large
                        size = getString(R.string.large)
                    }

                    PhotoSize.EXTRA_LARGE -> {
                        url = args.photoInfo.src.large2x
                        size = getString(R.string.extra_large)
                    }

                    PhotoSize.MEDIUM -> {
                        url = args.photoInfo.src.medium
                        size = getString(R.string.medium)
                    }

                    PhotoSize.ORIGINAL -> {
                        url = args.photoInfo.src.original
                        size = getString(R.string.original)
                    }

                    PhotoSize.PORTRAIT -> {
                        url = args.photoInfo.src.portrait
                        size = getString(R.string.portrait)
                    }

                    PhotoSize.SMALL -> {
                        url = args.photoInfo.src.small
                        size = getString(R.string.small)
                    }

                    PhotoSize.TINY -> {
                        url = args.photoInfo.src.tiny
                        size = getString(R.string.tiny)
                    }
                }
                setUpPhotoSize(size)
                try {
                    isLoading(true)
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
                                    isLoading(false)
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
                    navigate(
                        directions = PhotoDetailsFragmentDirections.actionPhotoDetailsFragmentToSelectPhotoSizeBottomSheetDialogFragment(
                            viewModel.photoSize.value ?: return@customSpan
                        ),
                        rootFragment = this@PhotoDetailsFragment
                    )
                })
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            isClickable = true
        }
    }

    private fun setUpPhotographerName() {
        val original = getString(R.string.photographer_name, args.photoInfo.photographer)
        val start = original.indexOf(args.photoInfo.photographer)
        binding.tvPhotographer.apply {
            text = SpannableString(original).customSpan(
                context = requireContext(),
                colorId = R.color.onPrimary,
                styleId = Typeface.BOLD,
                startPosition = start,
                endPosition = start + (args.photoInfo.photographer.length),
                isUnderLine = true,
                onClickSpan = {
                    navigate(
                        directions = PhotoDetailsFragmentDirections.actionGlobalWebViewFragment(
                            args.photoInfo.photographerUrl
                        ), rootFragment = this@PhotoDetailsFragment
                    )
                })
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
            isClickable = true
        }
    }

    override fun setResultListener() {
        setSelectSizeListener()
    }

    private fun setSelectSizeListener() {
        setFragmentResultListener(SelectPhotoSizeBottomSheetDialogFragment.SELECT_SIZE_REQUEST_KEY) { _, bundle ->
            findNavController().navigateUp()
            val size = PhotoSize.valueOf(
                bundle.getString(
                    SelectPhotoSizeBottomSheetDialogFragment.SIZE_BUNDLE_KEY,
                    PhotoSize.SMALL.name
                )
            )
            viewModel.updateSize(size)
        }
    }

    inner class ScaleListener : SimpleOnScaleGestureListener() {
        // when a scale gesture is detected, use it to resize the image
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            binding.ivPhoto.apply {
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
            return true
        }
    }
}