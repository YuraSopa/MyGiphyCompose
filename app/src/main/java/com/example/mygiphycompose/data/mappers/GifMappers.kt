package com.example.mygiphycompose.data.mappers

import com.example.mygiphycompose.data.local.GifEntity
import com.example.mygiphycompose.data.remote.GifsResponse
import com.example.mygiphycompose.domain.Gif

fun GifsResponse.toGifEntity(): List<GifEntity> {
    val list = mutableListOf<GifEntity>()
    data.mapTo(list) {
        GifEntity(
            embedUrl = it.embedUrl,
            id = it.id,
            image = it.images.fixedHeight.url,
            imageHeight = it.images.fixedWidth.height,
            isSticker = it.isSticker,
            rating = it.rating,
            title = it.title,
            url = it.url
        )
    }
    return list
}

fun GifEntity.toGif(): Gif {

    return Gif(
        embedUrl, id, image, imageHeight, isSticker, rating, title, url
    )
}
