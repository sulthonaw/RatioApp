package com.ratioapp.models

import androidx.compose.runtime.MutableState
import java.lang.Error

sealed interface ViewUiState<T> {
    data class Success<T>(val data: T) : ViewUiState<T>
    data class Error<T>(val error: Throwable?) : ViewUiState<T>
    data class Loading<T>(val isLoading: Boolean, val withPullRefresh: Boolean = false) : ViewUiState<T>
}

