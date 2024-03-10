package com.ratioapp.models

data class Album(
    val id: String,
    val user: User,
    val title: String,
    val description: String,
    val photos: List<Photo>?
)
