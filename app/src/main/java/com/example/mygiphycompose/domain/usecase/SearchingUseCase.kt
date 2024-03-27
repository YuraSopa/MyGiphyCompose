package com.example.mygiphycompose.domain.usecase

import com.example.mygiphycompose.domain.Gif
import com.example.mygiphycompose.domain.repository.GifRepository
import com.example.mygiphycompose.utils.Resource

class SearchingUseCase(
    private val repository: GifRepository
) {

    suspend fun execute(query: String, limit: Int, offset: Int): Resource<List<Gif>> {
        return repository.getSearchingGifs(query, limit, offset)
    }
}