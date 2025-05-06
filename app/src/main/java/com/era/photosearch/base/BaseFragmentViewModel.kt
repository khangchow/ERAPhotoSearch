package com.era.photosearch.base

import javax.inject.Inject

open class BaseFragmentViewModel<T : BaseEvent> @Inject constructor() : BaseViewModel<T>() {

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
//                sendExceptionToActivity(exception)
                null
            }
        }
    }

//    fun <T, E> APIResult<T, E>.execute(
//        onErrorException: (() -> Unit)? = null,
//        onErrorAPI: ((code: Int, message: String, errorData: E) -> Unit)? = null,
//        expectedErrorCode: List<Int> = emptyList(),
//        onSuccess: ((T) -> Unit),
//    ): T? {
//        return when (this) {
//            is APIResult.Success -> {
//                onSuccess.invoke(data)
//                data
//            }
//
//            is APIResult.ErrorException -> {
//                onErrorException?.invoke()
//                sendExceptionToActivity(exception)
//                null
//            }
//
//            is APIResult.ErrorAPI -> {
//                onErrorAPI?.invoke(code, message, errorData)
//                if (expectedErrorCode.contains(code).not()) sendExceptionToActivity()
//                null
//            }
//        }
//    }

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