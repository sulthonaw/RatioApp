package com.ratioapp.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Album
import com.ratioapp.models.Photo
import com.ratioapp.models.User
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ProfileViewModel : ViewModel() {
    var uiStateAlbums: ViewUiState<List<Album>> by mutableStateOf(ViewUiState.Loading(isLoading = true))

    var uiStatePhotos: ViewUiState<List<Photo>> by mutableStateOf(ViewUiState.Loading(isLoading = true))

    var uiStateUser: ViewUiState<User> by mutableStateOf(ViewUiState.Loading(isLoading = false))

    var following by mutableStateOf(0)
    var follower by mutableStateOf(0)
    var albumSize by mutableStateOf(0)

    var isLoadingFollow by mutableStateOf(true)
    var isFollow by mutableStateOf(false)

    fun getAlbums(
        token: String,
        userId: String,
        context: Context,
        snackbarHostState: SnackbarHostState
    ) {
        viewModelScope.launch {
            uiStateAlbums = try {
                val request =
                    RatioApi().userService.getAlbums(token = "Bearer $token", userId = userId)
                albumSize = request.data.size
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

    fun getUser(
        token: String,
        context: Context,
        userId: String,
        snackbarHostState: SnackbarHostState,
        withPullRefresh: Boolean = false
    ) {
        uiStateUser = ViewUiState.Loading(isLoading = true, withPullRefresh)
        viewModelScope.launch {
            uiStateUser = try {
                val request =
                    RatioApi().userService.getUser(token = "Bearer $token", userId = userId)
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Periksa Koneksi",
                    withDismissAction = true
                )
                ViewUiState.Error(e)
            }
        }
    }

    fun getPhotos(
        token: String,
        context: Context,
        snackbarHostState: SnackbarHostState,
        userId: String
    ) {
        viewModelScope.launch {
            uiStatePhotos = try {
                val request =
                    RatioApi().userService.getPhotos(token = "Bearer $token", userId = userId)
                ViewUiState.Success(request.data)
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

    fun logout(context: Context, navController: NavController) {
        viewModelScope.launch {
            AuthStore(context).clearAuth()
            navController.navigate(NavigationItem.Login.route)
        }
    }

    fun getFollowerAndFollowing(token: String, userId: String, userIdLoggin: String) {
        viewModelScope.launch {
            try {
                val requestFollower =
                    RatioApi().userService.getFollowers(token = "Bearer $token", userId = userId)
                follower = requestFollower.data.size
                isFollow = requestFollower.data.any { it.id == userIdLoggin }
                Log.d("ISFOLLOW", isFollow.toString())
                Log.d("ISFOLLOW", userId)
                val requestFollowing =
                    RatioApi().userService.getFollowing(token = "Bearer $token", userId = userId)
                following = requestFollowing.data.size
            } catch (e: HttpException) {
            } catch (e: IOException) {
            } finally {
                isLoadingFollow = false
            }
        }
    }

    fun followUser(token: String, userId: String, snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            try {
                isFollow = if (isFollow) {
                    RatioApi().userService.unfollow(token = "Bearer $token", userId = userId)
                    follower -= 1
                    false
                } else {
                    RatioApi().userService.follow(token = "Bearer $token", userId = userId)
                    follower += 1 
                    true
                }
            } catch (e: HttpException) {
            } catch (e: IOException) {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    withDismissAction = true
                )
            }
        }
    }
}