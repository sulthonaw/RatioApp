package com.ratioapp.api.services

import com.ratioapp.models.Comment
import com.ratioapp.models.Photo
import com.ratioapp.models.Response
import com.ratioapp.models.responseApi.CreateDonationResponse
import com.ratioapp.models.responseApi.LikeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PhotoService {
    @GET("photos/{photoId}")
    suspend fun getPhotos(
        @Header("Authorization") token: String,
        @Path("photoId") photoId: String
    ): Response<Photo>

    @GET("photos")
    suspend fun getPhotos(
        @Header("Authorization") token: String,
    ): Response<List<Photo>>


    @Multipart
    @POST("photos")
    suspend fun createPhoto(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): Response<Photo>

    @POST("photos/{photoId}/like")
    suspend fun like(
        @Header("Authorization") token: String,
        @Path("photoId") photoId: String
    ): Response<LikeResponse>

    @FormUrlEncoded
    @POST("photos/{photoId}/comentar")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Path("photoId") photoId: String,
        @Field("comentar") comment: String
    ): Response<Comment>

    @FormUrlEncoded
    @POST("/photos/{photoId}/donation")
    suspend fun createDonation(
        @Header("Authorization") token: String,
        @Path("photoId") photoId: String,
        @Field("amount") amount: Int
    ): Response<CreateDonationResponse>

    @DELETE("photos/{photoId}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("photoId") photoId: String
    ): Response<Any>
}
