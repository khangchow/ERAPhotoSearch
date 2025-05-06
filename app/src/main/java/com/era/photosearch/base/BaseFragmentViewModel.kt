package com.era.photosearch.base

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseFragmentViewModel<T : BaseEvent> @Inject constructor() : BaseViewModel<T>() {
    private val exceptionSharedFlow: MutableSharedFlow<Exception> = MutableSharedFlow()
    val exception: SharedFlow<Exception> get() = exceptionSharedFlow

    private fun sendExceptionToActivity(exception: Exception = Exception()) {
        viewModelScope.launch {
            exceptionSharedFlow.emit(exception)
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
                sendExceptionToActivity(exception)
                null
            }
        }
    }

    fun <T, E> APIResult<T, E>.execute(
        onErrorException: (() -> Unit)? = null,
        onErrorAPI: ((code: Int, message: String, errorData: E) -> Unit)? = null,
        expectedErrorCode: List<Int> = emptyList(),
        onSuccess: ((T) -> Unit),
    ): T? {
        return when (this) {
            is APIResult.Success -> {
                onSuccess.invoke(data)
                data
            }

            is APIResult.ErrorException -> {
                onErrorException?.invoke()
                sendExceptionToActivity(exception)
                null
            }

            is APIResult.ErrorAPI -> {
                onErrorAPI?.invoke(code, message, errorData)
                if (expectedErrorCode.contains(code).not()) sendExceptionToActivity()
                null
            }
        }
    }

    fun InputValidationResult.execute(
        onSuccess: (() -> Unit)? = null,
        onInvalid: (() -> Unit)? = null
    ): Boolean {
        return when (this) {
            InputValidationResult.Success -> {
                onSuccess?.invoke()
                true
            }

            InputValidationResult.InValid -> {
                onInvalid?.invoke()
                false
            }

            else -> false
        }
    }
}