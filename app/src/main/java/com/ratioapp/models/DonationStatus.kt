package com.ratioapp.models

enum class Status {
    SUCCESS, FAILED, PENDING
}
data class DonationStatus(
    val status: Status,
)
