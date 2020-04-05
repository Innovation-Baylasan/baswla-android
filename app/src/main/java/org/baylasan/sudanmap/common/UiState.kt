package org.baylasan.sudanmap.common

import androidx.lifecycle.MutableLiveData

sealed class UiState<T> {
    fun loading() {


    }

    /**
     * represents happy cases when a result is required.
     * @param data data required by the observer.
     */
    class Success<T>(val data: T) : UiState<T>()

    /**
     * represents happy cases when a result is not required.
     */
    class Complete<T> : UiState<T>()

    /**
     * represents an ongoing task that takes time.
     */
    class Loading<T> : UiState<T>()

    /**
     * represents an ongoing task return empty value.
     */
    class Empty<T> : UiState<T>()

    /**
     * represents an error occurred while a said operation is ongoing.
     */
    class Error<T>(val throwable: Throwable) : UiState<T>()
}

fun UiState<Any>.loading() :UiState<Any>{
    return UiState.Loading()
}