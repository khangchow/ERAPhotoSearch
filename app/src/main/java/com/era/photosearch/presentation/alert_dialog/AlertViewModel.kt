package com.era.photosearch.presentation.alert_dialog

import com.era.photosearch.base.BaseEvent
import com.era.photosearch.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlertViewModel @Inject constructor() : BaseViewModel<AlertEvent>() {

}

sealed class AlertEvent : BaseEvent() {

}