package com.ratioapp.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratioapp.api.RatioApi
import com.ratioapp.models.ViewUiState
import com.ratioapp.models.Wallet
import com.ratioapp.store.AuthStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class WalletViewModel : ViewModel() {

    var uiState: ViewUiState<Wallet> by mutableStateOf(ViewUiState.Loading(false))

    fun getWallet(token: String, withPullRefresh: Boolean = false) {
        uiState = ViewUiState.Loading(true, withPullRefresh)
        viewModelScope.launch {
            uiState = try {
                val request = RatioApi().walletService.getWallet(token = "Bearer $token")
                ViewUiState.Success(data = request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            }
        }
    }
}