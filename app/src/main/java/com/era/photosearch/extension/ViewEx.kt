package com.era.photosearch.extension

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onBottomReached(callback: () -> Unit): RecyclerView.OnScrollListener {
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (recyclerView.canScrollVertically(1).not()) {
                callback.invoke()
            }
        }
    }
    addOnScrollListener(listener)
    return listener
}