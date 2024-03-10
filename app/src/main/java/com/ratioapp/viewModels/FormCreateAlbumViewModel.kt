package com.ratioapp.viewModels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Response
import com.ratioapp.models.ViewUiState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class FormCreateAlbumViewModel : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var messagesError = mutableStateMapOf<String, String>()
    var uiState: ViewUiState<Nothing> by mutableStateOf(ViewUiState.Loading(isLoading = false))

    fun createAlbum(
        token: String,
        navController: NavController,
        snackbarHostState: SnackbarHostState,
        onDismissRequest: () -> Unit
    ) {
        viewModelScope.launch {
            messagesError.clear()
            try {
                uiState = ViewUiState.Loading(isLoading = true)

                RatioApi().albumService.createAlbum(
                    token = "Bearer $token",
                    title = title,
                    description = description
                )

                title = ""
                description = ""
                onDismissRequest()

            } catch (e: IOException) {
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    withDismissAction = true
                )
            } catch (e: HttpException) {
                val gson = Gson()
                val error =
                    gson.fromJson(e.response()?.errorBody()?.string(), Response::class.java)
                error?.errors?.messages?.map {
                    messagesError[it.field] = it.message
                }
            } finally {
                uiState = ViewUiState.Loading(isLoading = false)
            }
        }
    }
}