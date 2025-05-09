package com.era.photosearch.util

enum class PhotoSize {
    LANDSCAPE, LARGE, EXTRA_LARGE, MEDIUM, ORIGINAL, PORTRAIT, SMALL, TINY
}

object HttpStatus {
    const val TOO_MANY_REQUESTS = 429
    const val MISSING_QUERY_PARAM = 400
}