package com.example.mygiphycompose.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gif(
    val embedUrl: String,
    val id: String,
    val image: String,
    val imageHeight: String,
    val isSticker: Int,
    val rating: String,
    val title: String,
    val url: String
): Parcelable