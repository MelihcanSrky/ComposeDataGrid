package com.sarikaya.composedatagrid.gridindexselector

internal interface OnClickedEvent<D: Any> {
    fun onRowClicked(row: Int, data: D? = null, list: List<D>? = emptyList())
    fun onCellClicked(row: Int, cell: Int, data: String?)
}