package com.era.photosearch.extension

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.era.photosearch.util.CustomTypefaceSpan

fun SpannableString.customSpan(
    context: Context,
    fontId: Int? = null,
    colorId: Int? = null,
    styleId: Int? = null,
    startPosition: Int? = null,
    endPosition: Int? = null,
    isUnderLine: Boolean = false,
    onClickSpan: (() -> Unit)? = null,
): SpannableString {
    val start = startPosition ?: 0
    val end = endPosition ?: length
    if (fontId != null) {
        val fontFamily = ResourcesCompat.getFont(context, fontId)
        fontFamily?.let {
            val customTypefaceSpan = CustomTypefaceSpan(it)
            setSpan(
                customTypefaceSpan,
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    if (onClickSpan != null) {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickSpan.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = isUnderLine
            }
        }
        setSpan(
            clickableSpan,
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    if (colorId != null) {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorId, typedValue, true)
        val color = ContextCompat.getColor(context, typedValue.resourceId)
        setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    if (styleId != null) {
        setSpan(
            StyleSpan(styleId),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return this
}