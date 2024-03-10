package com.ratioapp.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class FormLoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var messagesError = mutableStateMapOf<String, String>()

    var uiState: ViewUiState<Nothing> by mutableStateOf(ViewUiState.Loading(isLoading = false))

    fun login(
        context: Context,
        navController: NavController,
        snackbarHostState: SnackbarHostState
    ) {
        messagesError.clear()
        viewModelScope.launch {
            try {
                uiState = ViewUiState.Loading(isLoading = true)
                val request = RatioApi().userService.login(email, password)
                AuthStore(context = context).setToken(token = request.data.token)
                AuthStore(context = context).setUserId(userId = request.data.user.id)
                navController.navigate(NavigationItem.Home.route)
            } catch (e: HttpException) {
                val gson = Gson()
                val response =
                    gson.fromJson(e.response()?.errorBody()?.string(), Response::class.java)
                response?.errors?.messages?.map {
                    messagesError[it.field] = it.message
                }
            } catch (e: IOException) {
                Log.e("Error Login", e.message.toString())
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    withDismissAction = true
                )
            } finally {
                uiState = ViewUiState.Loading(isLoading = false)
            }
        }
    }
}