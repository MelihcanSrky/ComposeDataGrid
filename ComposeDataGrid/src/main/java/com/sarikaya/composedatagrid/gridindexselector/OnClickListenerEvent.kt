package com.sarikaya.composedatagrid.gridindexselector

abstract class OnClickListenerEvent<D : Any> {
    abstract fun setOnRowClickListener(row: (Int) -> Unit)
    abstract fun setOnRowClickListener(row: (Int, D?) -> Unit)
    abstract fun setOnRowListClickListener(list: (Int, List<D>?) -> Unit)
    abstract fun setOnCellClickListener(cell: (Int, Int) -> Unit)
    abstract fun setOnCellClickListener(cell: (Int, Int, String) -> Unit)
}