package com.era.photosearch.model.ui

import android.os.Parcelable
import android.view.Gravity
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlertInfo(
    val title: String,
    val titleGravity: Int = Gravity.CENTER,
    val descriptionGravity: Int = Gravity.CENTER,
    val description: String,
    val positiveText: String,
    val negativeText: String? = null,
    val reason: String = "",
    val negativeReason: String = "",
    val isCancelable: Boolean = true
) : Parcelable