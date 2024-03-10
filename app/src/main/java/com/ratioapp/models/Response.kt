package com.ratioapp.models

import com.ratioapp.models.responseApi.MessageErrors

data class Response<T>(
    val status: Int,
    val message: String,
    val data: T,
    val errors: MessageErrors
)
