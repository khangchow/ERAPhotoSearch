package com.era.photosearch.data.remote

import com.era.photosearch.base.BasePagingResponse
import com.era.photosearch.model.response.PhotoInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): BasePagingResponse<PhotoInfoResponse>
}