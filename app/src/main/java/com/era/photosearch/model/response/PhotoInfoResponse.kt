package com.era.photosearch.model.response

import com.google.gson.annotations.SerializedName

data class PhotoInfoResponse(
    val alt: String?,
    @SerializedName("avg_color")
    val avgColor: String?,
    val height: Int?,
    val id: Long?,
    val liked: Boolean?,
    val photographer: String?,
    @SerializedName("photographer_id")
    val photographerId: Long?,
    @SerializedName("photographer_url")
    val photographerUrl: String?,
    val src: SrcResponse?,
    val url: String?,
    val width: Int?
)

data class PhotoInfo(
    val alt: String,
    val avgColor: String,
    val height: Int,
    val id: Long,
    val liked: Boolean,
    val photographer: String,
    val photographerId: Long,
    val photographerUrl: String,
    val src: Src,
    val url: String,
    val width: Int?
)

data class SrcResponse(
    val landscape: String?,
    val large: String?,
    val large2x: String?,
    val medium: String?,
    val original: String?,
    val portrait: String?,
    val small: String?,
    val tiny: String?
)

data class Src(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String
)

fun SrcResponse?.toSrc() = Src(
    landscape = this?.landscape.orEmpty(),
    large = this?.large.orEmpty(),
    large2x = this?.large2x.orEmpty(),
    medium = this?.medium.orEmpty(),
    original = this?.original.orEmpty(),
    portrait = this?.portrait.orEmpty(),
    small = this?.small.orEmpty(),
    tiny = this?.tiny.orEmpty()
)

fun PhotoInfoResponse?.toPhotoInfo() = PhotoInfo(
    alt = this?.alt.orEmpty(),
    avgColor = this?.avgColor.orEmpty(),
    height = this?.height ?: 0,
    id = this?.id ?: 0,
    liked = this?.liked == true,
    photographer = this?.photographer.orEmpty(),
    photographerId = this?.photographerId ?: 0,
    photographerUrl = this?.photographerUrl.orEmpty(),
    src = this?.src.toSrc(),
    url = this?.url.orEmpty(),
    width = this?.width ?: 0
)

fun List<PhotoInfoResponse?>?.toPhotos() = this?.map { it.toPhotoInfo() }.orEmpty()