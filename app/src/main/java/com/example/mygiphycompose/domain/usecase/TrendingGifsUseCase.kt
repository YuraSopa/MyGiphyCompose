package com.example.mygiphycompose.domain.usecase

import com.example.mygiphycompose.domain.Gif
import com.example.mygiphycompose.domain.repository.GifRepository
import com.example.mygiphycompose.utils.Resource

class TrendingGifsUseCase (
    private val repository: GifRepository
) {
    suspend fun execute(limit: Int, offset: Int): Resource<List<Gif>> {
        return repository.getTrendingGifs(limit, offset)
    }
}