package com.ratioapp.models

data class User(
    val id: String,
    val username: String,
    val email: String,
    val photoUrl: String,
    val fullName: String,
    val address: String,
    val albums: List<Album>?,
    val photos: List<Photo>?
)
