package com.sarikaya.composedatagrid.gridindexselector

internal class OnClickListenerImpl<D : Any> : OnClickListenerEvent<D>(), OnClickListeners<D>,
    OnClickedEvent<D> {
    override var rowOnClickListener: ((Int) -> Unit)? = null
    override var rowOnClickListenerWithData: ((Int, D?) -> Unit)? = null
    override var rowOnClickListenerWithList: ((Int, List<D>?) -> Unit)? = null
    override var cellOnClickListener: ((Int, Int) -> Unit)? = null
    override var cellOnClickListenerWithData: ((Int, Int, String) -> Unit)? = null


    override fun setOnRowClickListener(row: (Int) -> Unit) {
        rowOnClickListener = row
    }

    override fun setOnRowListClickListener(list: (Int, List<D>?) -> Unit) {
        rowOnClickListenerWithList = list
    }

    override fun setOnRowClickListener(row: (Int, D?) -> Unit) {
        rowOnClickListenerWithData = row
    }

    override fun setOnCellClickListener(cell: (Int, Int) -> Unit) {
        cellOnClickListener = cell
    }

    override fun setOnCellClickListener(cell: (Int, Int, String) -> Unit) {
        cellOnClickListenerWithData = cell
    }

    override fun onRowClicked(row: Int, data: D?, list: List<D>?) {
        if (rowOnClickListenerWithList != null) {
            rowOnClickListenerWithList?.invoke(row, list)
        } else if (rowOnClickListenerWithData != null) {
            rowOnClickListenerWithData?.invoke(row, data)
        } else {
            rowOnClickListener?.invoke(row)
        }
    }

    override fun onCellClicked(row: Int, cell: Int, data: String?) {
        if (cellOnClickListenerWithData != null) {
            cellOnClickListenerWithData?.invoke(row, cell, data!!)
        } else {
            cellOnClickListener?.invoke(row, cell)
        }
    }

    fun isCellNull(): Boolean {
        return cellOnClickListener != null || cellOnClickListenerWithData != null
    }

    fun isRowListNull(): Boolean {
        return rowOnClickListenerWithList == null
    }
}