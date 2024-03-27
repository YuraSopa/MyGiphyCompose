package com.example.mygiphycompose.data.remote


import com.google.gson.annotations.SerializedName

data class GifsResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("pagination")
    val pagination: Pagination
)