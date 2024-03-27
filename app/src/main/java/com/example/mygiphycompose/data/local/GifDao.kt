package com.example.mygiphycompose.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface GifDao {

    @Upsert
    suspend fun upsertAllGifs(gifs: List<GifEntity>)

    @Query("SELECT * FROM gifentity")
    suspend fun getAllGifs(): List<GifEntity>

    @Query("DELETE FROM gifentity")
    suspend fun clearAll()
}