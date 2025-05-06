package com.era.photosearch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.era.photosearch.presentation.MainViewModel
import kotlinx.coroutines.launch

abstract class BaseFragment<E : BaseEvent, VB : ViewBinding, VM : BaseFragmentViewModel<E>> :
    Fragment() {
    private var _binding: VB? = null
    abstract val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> VB)?
    abstract val viewModel: VM
    protected val mainViewModel: MainViewModel by activityViewModels()
    protected val binding get() = _binding!!

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
        bindEvent()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this.launch {
                    viewModel.exception.collect { exception ->
                        mainViewModel.handleException(exception)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    abstract fun bindComponent()
    abstract fun bindEvent()

    protected fun addMenuToCustomToolbar(
        toolbar: Toolbar,
        menuId: Int,
        onMenuItemClicked: (Int) -> Unit,
        overflowIconId: Int? = null
    ) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        overflowIconId?.let {
            toolbar.overflowIcon = ContextCompat.getDrawable(requireContext(), it)
        }
        val menuHost: MenuHost = requireActivity()
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuId, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                onMenuItemClicked(menuItem.itemId)
                return true
            }
        }
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}