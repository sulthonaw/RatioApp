package com.ratioapp.viewModels

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.gson.Gson
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Response
import com.ratioapp.models.User
import com.ratioapp.models.ViewUiState
import com.ratioapp.routes.NavigationItem
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class FormRegisterViewModel : ViewModel() {
    var uiState: ViewUiState<User> by mutableStateOf(ViewUiState.Loading(false))

    var stateScreen by mutableStateOf(1)


    var email by mutableStateOf("")
    var username by mutableStateOf("")
    var fullName by mutableStateOf("")
    var address by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var messagesError = mutableStateMapOf<String, String>()

    private fun passwordMatch(
        password: String,
        confirmPassword: String
    ): Boolean = password == confirmPassword

    fun createAccount(
        navController: NavController,
        snackbarHostState: SnackbarHostState
    ) {
        val passMatch = passwordMatch(password, confirmPassword)
        uiState = ViewUiState.Loading(true)
        if (passMatch)
            viewModelScope.launch {
                uiState = try {
                    val request = RatioApi().userService.createAccount(
                        email = email,
                        username = username,
                        fullName = fullName,
                        address = address,
                        password = password,
                        confirmPassword = password
                    )
                    email = ""
                    username = ""
                    fullName = ""
                    address = ""
                    password = ""
                    confirmPassword = ""
                    navController.navigate(NavigationItem.Home.route)
                    ViewUiState.Success(request.data)
                } catch (e: HttpException) {
                    val gson = Gson()
                    val response =
                        gson.fromJson(e.response()?.errorBody()?.string(), Response::class.java)
                    response?.errors?.messages?.map {
                        messagesError[it.field] = it.message
                    }
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = "Failed to register, check message error",
                        withDismissAction = true
                    )
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
}
