package com.ratioapp.models.responseApi

data class MessageErrors(val messages: List<MessageError>)

data class MessageError(val field: String, val message: String)
