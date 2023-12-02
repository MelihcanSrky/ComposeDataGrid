package com.sarikaya.composedatagrid.model

import com.sarikaya.composedatagrid.utils.ColumnType

data class DataGridColumn(
    val key: String,
    val name: String,
    val defaultVisibility: Boolean = true,
    val width: Int = 0,
    val type: ColumnType = ColumnType.TEXT,
)
