package com.ratioapp.api.services

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface WithDrawalService {
    @FormUrlEncoded
    @POST("withDrawals")
    suspend fun createWithDrawal(
        @Header("Authorization") token: String,
        @Field("amount") amount: Int,
        @Field("password") password: String
    )
}