package com.era.photosearch.presentation.webview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.era.photosearch.base.BaseFragment
import com.era.photosearch.databinding.FragmentWebviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment :
    BaseFragment<WebViewEvent, FragmentWebviewBinding, WebViewViewModel>() {
    override val bindingInflater: ((LayoutInflater, ViewGroup?, Boolean) -> FragmentWebviewBinding) =
        FragmentWebviewBinding::inflate
    override val viewModel: WebViewViewModel by viewModels()
    private val args: WebViewFragmentArgs by navArgs()

    override suspend fun eventObserver() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun bindComponent() {
        isLoading(true)
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = ERAWebViewClient { isLoading(false) }
            loadUrl(args.url)
        }
    }

    override fun setResultListener() {

    }
}