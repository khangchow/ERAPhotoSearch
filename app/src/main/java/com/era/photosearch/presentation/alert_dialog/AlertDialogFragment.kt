package com.era.photosearch.presentation.alert_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.era.photosearch.base.BaseDialogFragment
import com.era.photosearch.databinding.DialogAlertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertDialogFragment : BaseDialogFragment<AlertEvent, DialogAlertBinding, AlertViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> DialogAlertBinding) =
        DialogAlertBinding::inflate
    override val viewModel: AlertViewModel by viewModels()

    override suspend fun eventObserver() {

    }

    private val args: AlertDialogFragmentArgs by navArgs()

    override fun bindComponent() {
        super.bindComponent()
        binding.apply {
            if (args.info.negativeText != null) {
                btnNegative.isGone = false
                btnNegative.text = args.info.negativeText
            }
            tvTitle.apply {
                text = args.info.title
                gravity = args.info.titleGravity
            }
            tvDescription.apply {
                text = args.info.description
                gravity = args.info.descriptionGravity
            }
            btnPositive.text = args.info.positiveText
            isCancelable = args.info.isCancelable
            btnPositive.setOnClickListener {
                findNavController().navigateUp()
                if (args.info.reason.isNotEmpty()) {
                    setFragmentResult(
                        args.info.reason,
                        bundleOf()
                    )
                }
            }
            btnNegative.setOnClickListener {
                findNavController().navigateUp()
                if (args.info.negativeReason.isNotEmpty()) {
                    setFragmentResult(
                        args.info.negativeReason,
                        bundleOf()
                    )
                }
            }
        }
    }

    override fun setResultListener() {

    }
}