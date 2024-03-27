package com.example.mygiphycompose.domain.repository

import com.example.mygiphycompose.domain.Gif
import com.example.mygiphycompose.utils.Resource

interface GifRepository {
    suspend fun getTrendingGifs(limit: Int, offset: Int): Resource<List<Gif>>
    suspend fun getSearchingGifs(query: String, limit: Int, offset: Int): Resource<List<Gif>>
    suspend fun getCachedGifs(): Resource<List<Gif>>
}