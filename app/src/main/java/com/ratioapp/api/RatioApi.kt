package com.ratioapp.api

import com.ratioapp.api.services.AlbumService
import com.ratioapp.api.services.DonationService
import com.ratioapp.api.services.PhotoService
import com.ratioapp.api.services.UserService
import com.ratioapp.api.services.WalletService
import com.ratioapp.api.services.WithDrawalService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RatioApi {
    companion object {
        const val BASE_URL = "https://v5s5gzsg-4000.asse.devtunnels.ms"
        private val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(this.BASE_URL).build()
    }

    val photoService: PhotoService by lazy {
        retrofit.create(PhotoService::class.java)
    }
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
    val albumService: AlbumService by lazy {
        retrofit.create(AlbumService::class.java)
    }
    val donationService: DonationService by lazy {
        retrofit.create(DonationService::class.java)
    }
    val walletService: WalletService by lazy {
        retrofit.create(WalletService::class.java)
    }
    val withDrawalService: WithDrawalService by lazy {
        retrofit.create(WithDrawalService::class.java)
    }
}