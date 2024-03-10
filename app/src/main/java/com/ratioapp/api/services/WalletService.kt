package com.ratioapp.api.services

import com.ratioapp.models.Response
import com.ratioapp.models.Wallet
import retrofit2.http.GET
import retrofit2.http.Header

interface WalletService {
    @GET("wallet")
    suspend fun getWallet(
        @Header("Authorization") token: String
    ): Response<Wallet>
}