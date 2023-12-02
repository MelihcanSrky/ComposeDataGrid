package com.sarikaya.composedatagrid.extensions

import androidx.compose.runtime.MutableState


internal fun <D : Any> MutableMap<Int, D>.filterForIndexList(indexes: List<Int>): List<D> {
    val list = mutableListOf<D>()
    indexes.forEach { index ->
        this.forEach {
            if (it.key == index) {
                list.add(it.value)
            }
        }
    }
    return list
}

internal fun <D : Any> List<D>.setPagination(
    isPaginationEnabled: Boolean = false,
    isPaginationAsync: Boolean = false,
    pagingLimit: Int? = null,
    page: MutableState<Int>? = null
): List<D> {
    if (isPaginationEnabled && pagingLimit != null && page != null && !isPaginationAsync) {
        val startIndex = page.value * pagingLimit
        val endIndex = startIndex + pagingLimit
        return this.subList(startIndex, minOf(endIndex, this.size))
    }
    return this
}

internal fun Int.getPageCount(
    pagingLimit: Int
): Int {
    if ((this % pagingLimit) != 0) {
        return (this / pagingLimit) + 1
    }
    return this / pagingLimit
}

internal fun <D : Any> MutableMap<Int, D>.getPageCount(
    pagingLimit: Int
): Int {
    if ((this.size % pagingLimit) != 0) {
        return (this.size / pagingLimit) + 1
    }
    return this.size / pagingLimit
}