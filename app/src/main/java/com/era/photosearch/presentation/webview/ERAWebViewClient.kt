package com.era.photosearch.presentation.webview

import android.webkit.WebView
import android.webkit.WebViewClient

class ERAWebViewClient(
    private val onUrlChanged: (String) -> Unit,
) : WebViewClient() {
    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        super.doUpdateVisitedHistory(view, url, isReload)
        view?.url?.let(onUrlChanged)
    }
}