package com.sarikaya.composedatagrid.model

data class SelectableRowModel<D: Any>(
    val isSelected: Boolean = false,
    val data: D?
)