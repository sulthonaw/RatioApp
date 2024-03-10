package com.ratioapp.viewModels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ratioapp.api.RatioApi
import com.ratioapp.models.ViewUiState
import com.ratioapp.models.responseApi.CreateDonationResponse
import com.ratioapp.routes.NavigationItem
import com.ratioapp.store.AuthStore
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class MidtransViewModel : ViewModel() {
    var url by mutableStateOf<String?>(null)
    var paymentUiState: ViewUiState<CreateDonationResponse> by mutableStateOf(
        ViewUiState.Loading(
            false
        )
    )
    var amount by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun createDonation(
        token: String,
        photoId: String,
        amount: Int,
        navController: NavController,
        context: Context
    ) {
        isLoading = true
        paymentUiState = ViewUiState.Loading(true)
        viewModelScope.launch {
            paymentUiState = try {
                val request = RatioApi().photoService.createDonation(
                    token = "Bearer $token",
                    photoId = photoId,
                    amount = amount
                )
                AuthStore(context).setDonationId(request.data.donationId)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request.data.redirectUrl))
                context.startActivity(intent)
                navController.navigate(NavigationItem.StatusMidtrans.route + "/${request.data.donationId}/${token}")
                ViewUiState.Success(request.data)
            } catch (e: HttpException) {
                ViewUiState.Error(e)
            } catch (e: IOException) {
                ViewUiState.Error(e)
            } finally {
                isLoading = false
            }

        }
    }
}