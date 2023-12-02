package com.sarikaya.composedatagrid.gridindexselector

internal interface OnClickListeners<D: Any> {
    var rowOnClickListener: ((Int) -> Unit)?
    var rowOnClickListenerWithData: ((Int, D?) -> Unit)?
    var rowOnClickListenerWithList: ((Int, List<D>?) -> Unit)?
    var cellOnClickListener: ((Int, Int) -> Unit)?
    var cellOnClickListenerWithData: ((Int, Int, String) -> Unit)?
}