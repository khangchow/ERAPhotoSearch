package com.era.photosearch.presentation.webview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.era.photosearch.BuildConfig
import com.era.photosearch.R
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentWebviewBinding
import com.era.photosearch.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment :
    BaseFragment<WebViewEvent, FragmentWebviewBinding, WebViewViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> FragmentWebviewBinding) =
        FragmentWebviewBinding::inflate
    override val viewModel: WebViewViewModel by viewModels()
    private val args: WebViewFragmentArgs by navArgs()
    private var eraWebViewClient: ERAWebViewClient? = null

    override suspend fun eventObserver() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun bindComponent() {
        isLoading(true)
        setUpActionBar()
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            eraWebViewClient = ERAWebViewClient {
                (requireActivity() as MainActivity).supportActionBar?.title =
                    it.replace(BuildConfig.HOST, "")
                isLoading(false)
            }.also { webViewClient = it }
            loadUrl(args.url)
        }
    }

    private fun setUpActionBar() {
        val menuHost: MenuHost = requireActivity()
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_webview, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_refresh -> {
                        isLoading(true)
                        binding.webView.loadUrl(args.url)
                        true
                    }

                    else -> false
                }
            }
        }
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        eraWebViewClient?.removeCallback()
        eraWebViewClient = null
        binding.webView.stopLoading()
        super.onDestroyView()
    }

    override fun setResultListener() {

    }
}