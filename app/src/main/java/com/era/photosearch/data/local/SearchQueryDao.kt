package com.era.photosearch.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.era.photosearch.model.entity.SearchQueryEntity

@Dao
interface SearchQueryDao {
    @Insert
    suspend fun insert(searchQuery: SearchQueryEntity)

    @Query("DELETE FROM search_query WHERE content = :searchQuery")
    suspend fun deleteByQuery(searchQuery: String)

    @Query("DELETE FROM search_query WHERE id NOT IN (SELECT id FROM search_query ORDER BY id DESC LIMIT 50)")
    suspend fun deleteOlderQueries()

    @Transaction
    suspend fun upsertSearch(searchQuery: String) {
        deleteByQuery(searchQuery)
        insert(SearchQueryEntity(id = 0, content = searchQuery))
        deleteOlderQueries()
    }

    @Query("SELECT * FROM search_query WHERE content LIKE '%' || :searchQuery || '%' ORDER BY id DESC")
    fun getSearchQueries(searchQuery: String): PagingSource<Int, SearchQueryEntity>
}