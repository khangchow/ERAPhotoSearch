package com.era.photosearch.presentation.webview

import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor() : BaseViewModel<WebViewEvent>() {

}

sealed class WebViewEvent : BaseEvent() {

}