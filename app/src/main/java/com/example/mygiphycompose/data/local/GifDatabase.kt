package com.example.mygiphycompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GifEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GifDatabase : RoomDatabase() {

    abstract val dao: GifDao
}