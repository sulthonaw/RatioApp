package com.ratioapp.models

data class Photo(
    val id: String,
    val title: String,
    val description: String,
    val locationFile: String,
    val user: User?,
    val isLiked: Boolean,
    val albums: List<Album>,
    val comentars: List<Comment>?
)
