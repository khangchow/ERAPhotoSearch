package com.era.photosearch.base

import com.google.gson.annotations.SerializedName

data class BasePagingResponse<T>(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val photos: List<T>,
    @SerializedName("total_results")
    val totalResults: Long,
    @SerializedName("next_page")
    val nextPage: String
)