package com.sarikaya.composedatagrid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sarikaya.composedatagrid.R
import com.sarikaya.composedatagrid.extensions.setBorder
import com.sarikaya.composedatagrid.extensions.setHeight
import com.sarikaya.composedatagrid.extensions.setPadding
import com.sarikaya.composedatagrid.extensions.setWidth
import com.sarikaya.composedatagrid.theme.DataGridTheme
import com.sarikaya.composedatagrid.utils.SortBy
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
internal fun ColumnCell(
    text: String,
    index: Int,
    state: SnapshotStateMap<Int, Dp>,
    theme: DataGridTheme,
    isSortingEnabled: Boolean = false,
    sortBy: MutableState<MutableMap<String, SortBy>>,
    sortByKey: String,
    sortingButtonOnClick: (SortBy) -> Unit
) {
    val _sortBy = remember {
        mutableStateOf(sortBy.value[sortByKey])
    }
    val localDensity = LocalDensity.current
    val modifier = Modifier
        .background(theme.dataGridColors.headerRowColor)
        .onGloballyPositioned { coor ->
            state[index] = with(localDensity) { coor.size.width.toDp() }
        }
        .setWidth(state[index])
        .setHeight(theme.columnCellTextStyle.height)
        .setBorder(theme.dataGridColors.borderColor)
        .setPadding()
        .wrapContentHeight(align = Alignment.CenterVertically)
    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = theme.columnCellTextStyle.textStyle,
        )
        if (isSortingEnabled) {
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    when (_sortBy.value) {
                        SortBy.NO_SORT -> {
                            sortBy.value[sortByKey] = SortBy.ASCENDING
                            _sortBy.value = SortBy.ASCENDING
                        }

                        SortBy.ASCENDING -> {
                            sortBy.value[sortByKey] = SortBy.DESCENDING
                            _sortBy.value = SortBy.DESCENDING
                        }

                        SortBy.DESCENDING -> {
                            sortBy.value[sortByKey] = SortBy.NO_SORT
                            _sortBy.value = SortBy.NO_SORT
                        }

                        else -> {
                            sortBy.value[sortByKey] = SortBy.NO_SORT
                            _sortBy.value = SortBy.NO_SORT
                        }
                    }
                    sortingButtonOnClick(sortBy.value[sortByKey]!!)
                },
                Modifier
                    .setHeight(theme.columnCellTextStyle.height)
                    .setWidth(16.dp)
            ) {
                if (_sortBy.value == SortBy.NO_SORT) {
                    Icon(
                        painter = painterResource(id = R.drawable.sorting),
                        contentDescription = null,
                        tint = theme.columnCellTextStyle.textStyle.color,
                        modifier = Modifier.padding(2.dp)
                    )
                } else Icon(
                    imageVector = when (_sortBy.value) {
                        SortBy.ASCENDING -> Icons.Default.KeyboardArrowUp
                        SortBy.DESCENDING -> Icons.Default.KeyboardArrowDown
                        else -> Icons.Default.MoreVert
                    }, contentDescription = null,
                    tint = theme.columnCellTextStyle.textStyle.color
                )
            }
        }
    }
}