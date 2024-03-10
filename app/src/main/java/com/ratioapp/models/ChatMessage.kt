package com.ratioapp.models

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("send_on")
    val sendOn: Long,
    val from: String,
    val to: String,
    val message: String
)
