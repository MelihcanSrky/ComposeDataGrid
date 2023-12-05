package com.sarikaya.composedatagrid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.sarikaya.composedatagrid.model.DataGridColumn
import com.sarikaya.composedatagrid.theme.DataGridTheme
import com.sarikaya.composedatagrid.utils.SortBy

@Composable
internal fun ColumnGridRow(
    columns: MutableMap<Int, DataGridColumn>,
    columnWidths: SnapshotStateMap<Int, Dp>,
    theme: DataGridTheme,
    checkBoxEnabled: Boolean = false,
    checkBoxState: Boolean,
    isSortingEnabled: Boolean = false,
    sortingOnClickListener: (String, SortBy) -> Unit,
    sortBy: MutableState<MutableMap<String, SortBy>>,
    onCheckBoxClickListener: () -> Unit
) {
    Row {
        if (checkBoxEnabled) {
            CheckBoxColumn(
                checked = checkBoxState,
                onClick = {
                    onCheckBoxClickListener()
                },
                clearAllCheckBox = true,
                modifier = Modifier.background(theme.dataGridColors.headerRowColor),
                theme = theme
            )
        }
        columns.entries.toList().forEach { column ->
            if (column.value.defaultVisibility) {
                ColumnCell(
                    text = column.value.name,
                    index = column.key,
                    columnWidths,
                    theme,
                    isSortingEnabled,
                    sortBy = sortBy,
                    sortByKey = column.value.key
                ) {
                    sortingOnClickListener(column.value.key, it)
                }
            }
        }
    }
}