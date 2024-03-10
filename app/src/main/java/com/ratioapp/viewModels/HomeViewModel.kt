package com.ratioapp.viewModels

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Photo
import com.ratioapp.models.ViewUiState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException


class HomeViewModel : ViewModel() {
    var listPhotoViewUiState: ViewUiState<List<Photo>> by mutableStateOf(ViewUiState.Loading(false))
    var isScroll by mutableStateOf(false)
    fun getData(
        token: String,
        snackbarHostState: SnackbarHostState,
        withPullRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            listPhotoViewUiState = ViewUiState.Loading(true, withPullRefresh)
            isScroll = false
            listPhotoViewUiState = try {
                val request = RatioApi().photoService.getPhotos("Bearer $token")
                isScroll = true
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                isScroll = true
                ViewUiState.Error(e)
            } catch (e: IOException) {
                isScroll = true
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true
                )
                ViewUiState.Error(e)
            }
        }
    }
}