package com.example.mygiphycompose.data.remote.api

import com.example.mygiphycompose.BuildConfig
import com.example.mygiphycompose.data.remote.GifsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GifApi {

    @GET("trending")
    suspend fun getTrendingGifs(
        @Query("api_key")
        apiKey: String,
        @Query("limit")
        limit: Int,
        @Query("offset")
        offset: Int
    ): Response<GifsResponse>

    @GET("search")
    suspend fun getSearchGifs(
        @Query("api_key")
        apiKey: String,
        @Query("q")
        searchQuery: String,
        @Query("limit")
        limit: Int,
        @Query("offset")
        offset: Int
    ): Response<GifsResponse>

    companion object {
        const val BASE_URL = "https://api.giphy.com/v1/gifs/"
        const val API_KEY = BuildConfig.API_KEY

        private val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)

        }.build()
    }
}