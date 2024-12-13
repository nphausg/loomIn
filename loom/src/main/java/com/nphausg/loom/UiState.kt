package com.nphausg.loom

sealed class UiState<out T> {
    data object Init : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Loaded<T>(val data: T) : UiState<T>()
    data class Error(val throwable: Throwable? = null) : UiState<Nothing>()
}