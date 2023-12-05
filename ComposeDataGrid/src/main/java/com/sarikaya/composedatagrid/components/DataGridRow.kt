package com.sarikaya.composedatagrid.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.sarikaya.composedatagrid.extensions.setCellColor
import com.sarikaya.composedatagrid.extensions.setClickable
import com.sarikaya.composedatagrid.extensions.setConditionalColor
import com.sarikaya.composedatagrid.gridindexselector.OnClickListenerImpl
import com.sarikaya.composedatagrid.model.DataGridColumn
import com.sarikaya.composedatagrid.theme.DataGridTheme
import kotlin.reflect.KProperty1

@Composable
internal fun <D : Any> DataGridRow(
    rowIndex: MutableState<Int>,
    cellIndex: MutableState<Int>,
    index: Int,
    data: D,
    props: Collection<KProperty1<D, *>>,
    columnWidths: SnapshotStateMap<Int, Dp>,
    onClickSelector: OnClickListenerImpl<D>,
    theme: DataGridTheme,
    columns: MutableMap<Int, DataGridColumn>,
    checkBoxEnabled: Boolean = false,
    selectedIndexList: List<Int>,
    onSelectedRowChange: (Boolean) -> Unit
) {
    val isSelected = remember {
        mutableStateOf(false)
    }
    Row(
        Modifier
            .setConditionalColor(
                (selectedIndexList.any { it == index }),
                theme.dataGridColors.selectedRowColor()
            )
            .setClickable {
                isSelected.value = !isSelected.value
                onSelectedRowChange(isSelected.value)
            }
    ) {
        if (checkBoxEnabled) {
            CheckBoxColumn(
                checked = selectedIndexList.any { it.equals(index) },
                theme = theme
            )
        }
        columns.entries.forEachIndexed { cIndex, column ->
            if (column.value.defaultVisibility) {
                var value = ""
                for (prop in props) {
                    if (prop.name == column.value.key) {
                        value = prop.getter.call(data).toString()
                    }
                }
                DataCell(
                    text = "$value",
                    cIndex,
                    columnWidths,
                    if (onClickSelector.isCellNull()) Modifier
                        .setCellColor(
                            rowIndex.value == index,
                            cellIndex.value == cIndex,
                            theme.dataGridColors
                        )
                        .setClickable {
                            rowIndex.value = index
                            cellIndex.value = cIndex
                            onClickSelector.onCellClicked(index, cIndex, value)
                        }
                    else Modifier,
                    theme = theme
                )
            }
        }
    }
}