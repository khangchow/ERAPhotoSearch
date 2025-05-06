package com.era.photosearch.util

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
    override fun updateDrawState(ds: TextPaint) {
        apply(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        apply(paint, typeface)
    }

    private fun apply(paint: TextPaint, typeface: Typeface) {
        paint.typeface = typeface
    }
}