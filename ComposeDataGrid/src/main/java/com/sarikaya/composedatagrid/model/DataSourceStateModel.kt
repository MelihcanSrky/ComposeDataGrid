package com.sarikaya.composedatagrid.model

internal data class DataSourceState<D : Any>(
    val index: Int,
    val data: D
)