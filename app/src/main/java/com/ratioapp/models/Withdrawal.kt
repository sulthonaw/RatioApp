package com.ratioapp.models

import java.time.temporal.TemporalAmount
import java.util.Date

data class Withdrawal(
    val id: String,
    val amount: Int,
    val user: User,
    val createdAt: Date
)
