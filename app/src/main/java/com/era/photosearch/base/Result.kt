package com.era.photosearch.base

sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()

    class ErrorException<T>(val exception: Exception) : Result<T>()
}

suspend fun <Data> getResult(action: suspend () -> Data): Result<Data> {
    return try {
        Result.Success(action.invoke())
    } catch (e: Exception) {
        Result.ErrorException(e)
    }
}

sealed class InputValidationResult {
    data object Success : InputValidationResult()
    data object InValid : InputValidationResult()
    data object None : InputValidationResult()
}
