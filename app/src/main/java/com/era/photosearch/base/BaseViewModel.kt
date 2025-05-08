package com.era.photosearch.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseViewModel<T : BaseEvent> @Inject constructor() : ViewModel() {
    private val eventSharedFlow: MutableSharedFlow<T> = MutableSharedFlow()
    val event: SharedFlow<T> get() = eventSharedFlow

    protected fun sendEvent(event: T) {
        viewModelScope.launch {
            eventSharedFlow.emit(event)
        }
    }

    fun <T> Result<T>.execute(
        onErrorException: (() -> Unit)? = null,
        onSuccess: ((T) -> Unit)
    ): T? {
        return when (this) {
            is Result.Success -> {
                onSuccess.invoke(data)
                data
            }

            is Result.ErrorException -> {
                onErrorException?.invoke()
                null
            }
        }
    }
}

open class BaseEvent