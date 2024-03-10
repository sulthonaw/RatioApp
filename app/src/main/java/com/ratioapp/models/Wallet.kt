package com.ratioapp.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Wallet(
    val id: String,
    val amount: Int,
    @SerializedName("withDrawals")
    val withdrawals: List<Withdrawal>,
    val donation: List<Donation>,
    val createdAt: Date
)
