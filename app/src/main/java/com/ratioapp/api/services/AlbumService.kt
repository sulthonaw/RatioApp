package com.ratioapp.api.services

import com.ratioapp.models.Album
import com.ratioapp.models.Response
import com.ratioapp.models.responseApi.AddedToAlbum
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AlbumService {

    @GET("albums")
    suspend fun getAlbumsMe(@Header("Authorization") token: String): Response<List<Album>>

    @GET("albums/{albumId}")
    suspend fun getDetailAlbumMe(
        @Header("Authorization") token: String,
        @Path("albumId") albumId: String
    ): Response<Album>

    @FormUrlEncoded
    @POST("albums")
    suspend fun createAlbum(
        @Header("Authorization") token: String,
        @Field("title") title: String,
        @Field("description") description: String
    ): Response<Nothing>

    @POST("albums/{albumId}/photos/{photoId}")
    suspend fun addPhotoToAlbum(
        @Header("Authorization") token: String,
        @Path("albumId") albumId: String,
        @Path("photoId") photoId: String
    ): Response<AddedToAlbum>

    @DELETE("albums/{albumId}")
    suspend fun deleteAlbum(
        @Header("Authorization") token: String,
        @Path("albumId") albumId: String
    ): Response<Nothing>
}