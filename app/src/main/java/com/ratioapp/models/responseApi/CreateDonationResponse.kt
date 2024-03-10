package com.ratioapp.models.responseApi

data class CreateDonationResponse(
    val donationId: String,
    val token: String,
    val redirectUrl: String
)
