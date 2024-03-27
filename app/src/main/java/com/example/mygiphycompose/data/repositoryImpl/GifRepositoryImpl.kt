package com.example.mygiphycompose.data.repositoryImpl

import com.example.mygiphycompose.data.local.GifDatabase
import com.example.mygiphycompose.data.mappers.toGif
import com.example.mygiphycompose.data.mappers.toGifEntity
import com.example.mygiphycompose.data.remote.api.GifApi
import com.example.mygiphycompose.data.remote.api.GifApi.Companion.API_KEY
import com.example.mygiphycompose.domain.Gif
import com.example.mygiphycompose.domain.repository.GifRepository
import com.example.mygiphycompose.utils.Resource
import javax.inject.Inject

class GifRepositoryImpl @Inject constructor(
    private val db: GifDatabase,
    private val api: GifApi
) : GifRepository {
    override suspend fun getTrendingGifs(limit: Int, offset: Int): Resource<List<Gif>> {

        val response = api.getTrendingGifs(API_KEY, limit, offset)

        if (response.isSuccessful) {
            val gifData = response.body()!!

            if (offset == 0) db.dao.clearAll()

            db.dao.upsertAllGifs(
                gifData.toGifEntity()
            )

            return Resource.Success(
                gifData.toGifEntity().map {
                    it.toGif()
                })
        }
        return Resource.Error(response.message())
    }

    override suspend fun getSearchingGifs(
        query: String,
        limit: Int,
        offset: Int
    ): Resource<List<Gif>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCachedGifs(): Resource<List<Gif>> {
        val list = db.dao.getAllGifs().map {
            it.toGif()
        }
        return Resource.Success(list)
    }
}