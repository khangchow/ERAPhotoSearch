package com.era.photosearch.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_query")
data class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val content: String
)