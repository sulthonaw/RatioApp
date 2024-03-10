package com.ratioapp.viewModels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratioapp.api.RatioApi
import com.ratioapp.models.Status
import com.ratioapp.store.AuthStore
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class StatusMidtransViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
//    val donationId: String = checkNotNull(savedStateHandle["donationId"])
//    val token: String = checkNotNull(savedStateHandle["token"])

    var donationStatus by mutableStateOf(Status.PENDING)
    var isLoading by mutableStateOf(false)

    fun getStatus(donationId: String, token: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                val request = RatioApi().donationService.getStatusDonation(
                    token = "Bearer $token",
                    donationId = donationId
                )
                donationStatus = request.data.status
            } catch (e: HttpException) {
            } catch (e: IOException) {
            } finally {
                isLoading = false
            }
        }
    }
}