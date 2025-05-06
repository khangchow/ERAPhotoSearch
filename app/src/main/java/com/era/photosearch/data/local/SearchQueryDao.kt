package com.era.photosearch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.era.photosearch.model.entity.SearchQueryEntity

@Dao
interface SearchQueryDao {
    @Insert
    suspend fun insert(searchQuery: SearchQueryEntity): Long

    @Query("DELETE FROM search_query WHERE content = :searchQuery")
    suspend fun deleteByQuery(searchQuery: String)

    @Transaction
    suspend fun upsertSearch(searchQuery: String) {
        deleteByQuery(searchQuery)
        insert(SearchQueryEntity(id = 0, content = searchQuery))
    }
}