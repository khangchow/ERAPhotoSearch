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
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentPhotoDetailsBinding
import com.era.photosearch.extension.customSpan
import com.era.photosearch.extension.navigate
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
            tvSize.text =
                getString(R.string.photo_size, args.photoInfo.width, args.photoInfo.height)
        }
    }

    private fun setUpPhoto() {
        binding.ivPhoto.apply {
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
            Glide.with(requireContext()).load(args.photoInfo.src.original).dontTransform().listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                }
            ).into(this)
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
                endPosition = start + args.photoInfo.photographer.length,
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

    }
}