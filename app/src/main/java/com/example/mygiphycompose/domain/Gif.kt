package com.example.mygiphycompose.domain

data class Gif(
    val embedUrl: String,
    val id: String,
    val image: String,
    val imageHeight: String,
    val isSticker: Int,
    val rating: String,
    val title: String,
    val url: String
)