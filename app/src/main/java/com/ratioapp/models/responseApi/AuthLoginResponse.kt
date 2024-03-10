package com.ratioapp.models.responseApi

import com.ratioapp.models.User

data class AuthLoginResponse(val token: String, val user: User)
