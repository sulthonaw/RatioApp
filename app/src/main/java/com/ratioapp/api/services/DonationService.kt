package com.ratioapp.api.services

import com.ratioapp.models.DonationStatus
import com.ratioapp.models.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DonationService {
    @GET("donation/{donationId}/status")
    suspend fun getStatusDonation(
        @Header("Authorization") token: String,
        @Path("donationId") donationId: String
    ): Response<DonationStatus>
}