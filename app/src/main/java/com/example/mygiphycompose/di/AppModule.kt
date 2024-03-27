package com.example.mygiphycompose.di

import android.content.Context
import androidx.room.Room
import com.example.mygiphycompose.data.local.GifDatabase
import com.example.mygiphycompose.data.remote.api.GifApi
import com.example.mygiphycompose.utils.NetworkConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGifsDatabase(@ApplicationContext context: Context): GifDatabase {
        return Room.databaseBuilder(
            context,
            GifDatabase::class.java,
            "gifs.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGifsApi(): GifApi {
        return Retrofit.Builder()
            .baseUrl(GifApi.BASE_URL)
            .client(GifApi.client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext context: Context) = NetworkConnection(context)

}