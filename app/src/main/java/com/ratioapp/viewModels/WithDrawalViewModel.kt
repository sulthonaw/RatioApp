package com.ratioapp.viewModels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ratioapp.api.RatioApi
import com.ratioapp.models.ViewUiState
import com.ratioapp.models.Wallet
import com.ratioapp.routes.NavigationItem
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class WithDrawalViewModel : ViewModel() {
    var amount by mutableStateOf("")
    var password by mutableStateOf("")

    var uiState: ViewUiState<Wallet> by mutableStateOf(ViewUiState.Loading(false))
    fun getWallet(token: String, withPullRefresh: Boolean = false) {
        viewModelScope.launch {
            uiState = ViewUiState.Loading(true, withPullRefresh)
            uiState = try {
                val request = RatioApi().walletService.getWallet(token = "Bearer $token")
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            }
        }
    }

    fun createWithDrawal(
        token: String,
        navController: NavController,
        snackbarHostState: SnackbarHostState
    ) {
        viewModelScope.launch {
            try {
                val request = RatioApi().withDrawalService.createWithDrawal(
                    token = "Bearer $token",
                    amount = amount.toInt(),
                    password = password
                )
                navController.navigate(NavigationItem.Wallet.route)
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Berhasil ditarik",
                    withDismissAction = true
                )
            } catch (e: HttpException) {
                navController.navigate(NavigationItem.Wallet.route)
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Gagal ditarik, silahkan coba lagi",
                    withDismissAction = true
                )
            } catch (e: IOException) {
                navController.navigate(NavigationItem.Wallet.route)
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = "Periksa koneksi",
                    withDismissAction = true
                )
            } finally {
                amount = ""
                password = ""
            }
        }
    }
}