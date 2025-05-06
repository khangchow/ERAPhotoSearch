package com.era.photosearch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.era.photosearch.model.entity.SearchQueryEntity

@Database(entities = [SearchQueryEntity::class], version = 1, exportSchema = false)
abstract class ERADatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
}