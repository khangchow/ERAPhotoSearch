package com.era.photosearch.base

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.era.photosearch.presentation.MainViewModel
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

abstract class BaseBottomSheetDialogFragment<E : BaseEvent, VB : ViewBinding, vm : BaseViewModel<E>> :
    BottomSheetDialogFragment() {
    private var _binding: VB? = null
    abstract val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> VB)?
    abstract val viewModel: vm
    protected val mainViewModel: MainViewModel by activityViewModels()
    protected val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater?.invoke(inflater, container, false)
        return _binding?.root
    }

    abstract suspend fun eventObserver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShowFullContent()
        bindComponent()
        setResultListener()
        lifecycleScope.launch { eventObserver() }
    }

    private fun setShowFullContent() {
        val bottomSheet =
            dialog?.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.apply {
                peekHeight = Resources.getSystem().displayMetrics.heightPixels
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun bindComponent()
    abstract fun setResultListener()
}