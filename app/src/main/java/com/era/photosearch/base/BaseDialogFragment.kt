package com.era.photosearch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.era.photosearch.R
import com.era.photosearch.presentation.MainViewModel
import kotlinx.coroutines.launch

abstract class BaseDialogFragment<E : BaseEvent, VB : ViewBinding, vm : BaseViewModel<E>> :
    DialogFragment() {
    private var _binding: VB? = null
    abstract val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> VB)?
    abstract val viewModel: vm
    val mainViewModel: MainViewModel by activityViewModels()
    protected val binding get() = _binding!!

    abstract suspend fun eventObserver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.inset_dialog)
        _binding = bindingInflater?.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onStart() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindComponent()
        setResultListener()
        lifecycleScope.launch { eventObserver() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun bindComponent() {}
    abstract fun setResultListener()
}