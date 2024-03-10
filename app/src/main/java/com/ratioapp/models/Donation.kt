package com.ratioapp.models

import java.util.Date

data class Donation(
    val id: String, val amount: Int, val user: User,
    val createdAt: Date
)
