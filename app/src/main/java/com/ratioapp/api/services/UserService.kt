package com.ratioapp.api.services

import com.ratioapp.models.Album
import com.ratioapp.models.Photo
import com.ratioapp.models.Response
import com.ratioapp.models.User
import com.ratioapp.models.responseApi.AuthLoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {

    @GET("users/{userId}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<User>

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<List<User>>

    @GET("users/{userId}/photos")
    suspend fun getPhotos(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<Photo>>

    @GET("users/{userId}/following")
    suspend fun getFollowing(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<User>>

    @GET("users/{userId}/followers")
    suspend fun getFollowers(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<User>>

    @FormUrlEncoded
    @POST("users/auth/register")
    suspend fun createAccount(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("fullname") fullName: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String,
        @Field("address") address: String
    ): Response<User>

    @GET("users/{userId}/albums")
    suspend fun getAlbums(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<Album>>

    @FormUrlEncoded
    @POST("users/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthLoginResponse>

    @Multipart
    @PUT("users/profile")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Part("fullName") fullName: RequestBody,
        @Part("address") address: RequestBody,
        @Part("username") username: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): Response<User>

    @POST("users/{userId}/follow")
    suspend fun follow(@Header("Authorization") token: String, @Path("userId") userId: String)

    @DELETE ("users/{userId}/unfollow")
    suspend fun unfollow(@Header("Authorization") token: String, @Path("userId") userId: String)
}