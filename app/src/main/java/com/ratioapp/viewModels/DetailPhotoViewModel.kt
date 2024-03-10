package com.ratioapp.viewModels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Album
import com.ratioapp.models.Comment
import com.ratioapp.models.Photo
import com.ratioapp.models.ViewUiState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class DetailPhotoViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val photoId: String = checkNotNull(savedStateHandle["photoId"])

    var comment by mutableStateOf("")
    var photoUiState: ViewUiState<Photo> by mutableStateOf(ViewUiState.Loading(isLoading = false))
    var albumsUiState: ViewUiState<List<Album>> by mutableStateOf(ViewUiState.Loading(isLoading = false))
    var commentUiState: ViewUiState<Comment> by mutableStateOf(ViewUiState.Loading(isLoading = false))
    var isLiked by mutableStateOf(false)

    fun getPhoto(
        token: String,
        photoId: String,
        snackbarHostState: SnackbarHostState,
        withPullRefresh: Boolean = false
    ) {
        photoUiState = ViewUiState.Loading(isLoading = true, withPullRefresh = withPullRefresh)
        viewModelScope.launch {
            photoUiState = try {
                val request = RatioApi().photoService.getPhotos("Bearer $token", photoId)
                isLiked = request.data.isLiked
                ViewUiState.Success(data = request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    withDismissAction = true
                )
                ViewUiState.Error(e)
            }
        }
    }

    fun getAlbum(token: String, userId: String) {
        albumsUiState = ViewUiState.Loading(isLoading = true)
        viewModelScope.launch {
            albumsUiState = try {
                val request =
                    RatioApi().userService.getAlbums(token = "Bearer $token", userId = userId)
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            }
        }

    }

    fun like(token: String, photoId: String, snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            try {
                val request =
                    RatioApi().photoService.like(token = "Bearer $token", photoId = photoId)
                isLiked = request.data.isLiked
            } catch (e: HttpException) {
            } catch (e: IOException) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(message = "Like gagal", withDismissAction = true)
            }
        }
    }

    fun createComment(token: String, photoId: String, snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            commentUiState = try {
                val request = RatioApi().photoService.createComment(
                    token = "Bearer $token",
                    photoId = photoId,
                    comment = comment
                )
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(message = "HTTP", withDismissAction = true)
                ViewUiState.Error(e)
            } catch (e: IOException) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(message = "Periksa koneksi", withDismissAction = true)
                ViewUiState.Error(e)
            }
            comment = ""
        }
    }
}