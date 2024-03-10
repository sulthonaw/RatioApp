package com.ratioapp.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratioapp.api.RatioApi
import com.ratioapp.models.User
import com.ratioapp.models.ViewUiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetailFollowViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    var uiStateFollowers: ViewUiState<List<User>> by mutableStateOf(ViewUiState.Loading(false))
    var uiStateFollowing: ViewUiState<List<User>> by mutableStateOf(ViewUiState.Loading(false))


    var uiStateUser: ViewUiState<User> by mutableStateOf(ViewUiState.Loading(isLoading = false))

    fun getUser(
        token: String,
        userId: String,
    ) {
        viewModelScope.launch {
            uiStateUser = try {
                val request =
                    RatioApi().userService.getUser(token = "Bearer $token", userId = userId)
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            }
        }
    }

    fun getFollowerAndFollowing(token: String, userId: String) {
        viewModelScope.launch {
            try {
                val requestFollower =
                    RatioApi().userService.getFollowers(token = "Bearer $token", userId = userId)
                uiStateFollowers = ViewUiState.Success(requestFollower.data)
                val requestFollowing =
                    RatioApi().userService.getFollowing(token = "Bearer $token", userId = userId)
                uiStateFollowing = ViewUiState.Success(requestFollowing.data)
            } catch (e: HttpException) {
                uiStateFollowing = ViewUiState.Error(e)
            } catch (e: IOException) {
                uiStateFollowing = ViewUiState.Error(e)
            }
        }
    }
}