package com.ratioapp.viewModels

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ratioapp.api.RatioApi
import com.ratioapp.models.User
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.HttpException

class FormEditProfileViewModel : ViewModel() {
    var fullName by mutableStateOf("")
    var username by mutableStateOf("")
    var address by mutableStateOf("")
    var email by mutableStateOf("")
    var photoUri by mutableStateOf<Uri?>(null)
    var photoUrl by mutableStateOf("")

    var uiState: ViewUiState<User> by mutableStateOf(ViewUiState.Loading(false))
    var isLoading by mutableStateOf(false)

    fun getUser(
        token: String,
        snackbarHostState: SnackbarHostState,
        userId: String,
        withPullRefresh: Boolean = false
    ) {
        uiState = ViewUiState.Loading(true, withPullRefresh)
        viewModelScope.launch {
            uiState = try {
                val request = RatioApi().userService.getUser(token = "Bearer $token", userId)
                fillTextField(request.data)
                ViewUiState.Success(request.data)
            } catch (e: IOException) {
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    withDismissAction = true
                )
                ViewUiState.Error(e)
            }
        }
    }

    private fun fillTextField(user: User) {
        fullName = user.fullName
        username = user.username
        address = user.address
        email = user.email
        photoUrl = "${RatioApi.BASE_URL}/files/images/profiles/${user.photoUrl}"
    }

    fun updateUser(
        token: String,
        snackbarHostState: SnackbarHostState,
        navController: NavController
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                val request = RatioApi().userService.updateUser(
                    token = "Bearer $token",
                    username = this@FormEditProfileViewModel.username.toRequestBody("text/plain".toMediaTypeOrNull()),
                    address = this@FormEditProfileViewModel.address.toRequestBody("text/plain".toMediaTypeOrNull()),
                    fullName = this@FormEditProfileViewModel.fullName.toRequestBody("text/plain".toMediaTypeOrNull()),
                )
                navController.navigate(NavigationItem.ProfileMe.route)
            } catch (e: HttpException) {
                snackbarHostState.currentSnackbarData?. dismiss()
                snackbarHostState.showSnackbar(
                    message = "Gagal update",
                    withDismissAction = true
                )
            } catch (e: IOException) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    withDismissAction = true
                )
            } finally {
                isLoading = false
            }
        }
    }
}