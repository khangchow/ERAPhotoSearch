package com.era.photosearch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.era.photosearch.presentation.MainActivity
import com.era.photosearch.presentation.MainViewModel
import kotlinx.coroutines.launch

abstract class BaseFragment<E : BaseEvent, VB : ViewBinding, VM : BaseViewModel<E>> :
    Fragment() {
    private var _binding: VB? = null
    abstract val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> VB)?
    abstract val viewModel: VM
    protected val mainViewModel: MainViewModel by activityViewModels()
    protected val binding get() = _binding!!
    abstract suspend fun eventObserver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater?.invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindComponent()
        setResultListener()
        lifecycleScope.launch { eventObserver() }
    }

    protected fun isLoading(isLoading: Boolean) {
        try {
            (requireActivity() as MainActivity).isLoading(isLoading)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        isLoading(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun bindComponent()
    abstract fun setResultListener()
}