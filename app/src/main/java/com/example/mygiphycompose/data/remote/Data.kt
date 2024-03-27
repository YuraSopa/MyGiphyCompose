package com.example.mygiphycompose.data.remote


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("embed_url")
    val embedUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: Images,
    @SerializedName("is_sticker")
    val isSticker: Int,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)