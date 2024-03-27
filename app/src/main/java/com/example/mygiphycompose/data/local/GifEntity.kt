package com.example.mygiphycompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GifEntity(
    val embedUrl: String,
    @PrimaryKey
    val id: String,
    val image: String,
    val imageHeight: String,
    val isSticker: Int,
    val rating: String,
    val title: String,
    val url: String
)