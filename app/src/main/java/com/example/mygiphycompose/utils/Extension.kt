package com.example.mygiphycompose.utils

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.MutableState

fun LazyStaggeredGridState.shouldLoadMore(rememberedIndex: MutableState<Int>, threshold: Int): Boolean {
    val firstVisibleIndex = this.firstVisibleItemIndex
    if (rememberedIndex.value != firstVisibleIndex) {
        rememberedIndex.value = firstVisibleIndex
        val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        return lastVisibleIndex >= layoutInfo.totalItemsCount - 1 - threshold
    }
    return false
}