package com.sarikaya.composedatagrid.extensions

import androidx.compose.runtime.MutableState
import com.sarikaya.composedatagrid.model.DataSourceState
import com.sarikaya.composedatagrid.utils.SortBy
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties


internal fun <D : Any> List<DataSourceState<D>>.filterForIndexList(indexes: List<Int>): List<D> {
    val list = mutableListOf<D>()
    indexes.forEach { index ->
        this.forEach {
            if (it.index == index) {
                list.add(it.data)
            }
        }
    }
    return list
}

internal fun <D : Any> List<DataSourceState<D>>.setPagination(
    isPaginationEnabled: Boolean = false,
    isPaginationAsync: Boolean = false,
    pagingLimit: Int? = null,
    page: MutableState<Int>? = null
): List<DataSourceState<D>> {
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

internal fun <D : Any> List<D>.getPageCount(
    pagingLimit: Int
): Int {
    if ((this.size % pagingLimit) != 0) {
        return (this.size / pagingLimit) + 1
    }
    return this.size / pagingLimit
}

internal fun <D : Any> List<DataSourceState<D>>.sortingBy(
    sortBy: MutableState<MutableMap<String, SortBy>>
): List<DataSourceState<D>> {
    val _sortBy = sortBy.value.entries.firstOrNull { it.value != SortBy.NO_SORT }
    return if (_sortBy == null) {
        println("NO SORT")
        this
    } else if (_sortBy.value == SortBy.ASCENDING) {
        println("ASCENDING")
        this.sortedBy {
            val prop = it.data::class.java.getDeclaredField(_sortBy.key)
            prop.isAccessible = true
            val value = prop.get(it.data)
            value as Comparable<Any>
        }
    } else if (_sortBy.value == SortBy.DESCENDING) {
        println("DESCENDING")
        this.sortedByDescending {
            val prop = it.data::class.java.getDeclaredField(_sortBy.key)
            prop.isAccessible = true
            val value = prop.get(it.data)
            value as Comparable<Any>
        }
    } else {
        println("ELSE")
        this
    }
}