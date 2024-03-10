package com.ratioapp.libs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ratioapp.models.ViewUiState


@Composable
fun whenUiState(
    uiState: ViewUiState<*>,
    onLoading: @Composable () -> Unit,
    onError: @Composable () -> Unit,
    onSuccess: @Composable () -> Unit,
) {
    when (uiState) {
        is ViewUiState.Success -> {
            onSuccess()
        }

        is ViewUiState.Loading -> {
            onLoading()
        }

        is ViewUiState.Error -> {
            onError()
        }

        else -> {
            Text(text = "ELSE")
        }
    }
}