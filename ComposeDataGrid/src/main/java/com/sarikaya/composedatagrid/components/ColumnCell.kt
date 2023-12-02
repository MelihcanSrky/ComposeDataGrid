package com.sarikaya.composedatagrid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sarikaya.composedatagrid.extensions.setBorder
import com.sarikaya.composedatagrid.extensions.setHeight
import com.sarikaya.composedatagrid.extensions.setPadding
import com.sarikaya.composedatagrid.extensions.setWidth
import com.sarikaya.composedatagrid.theme.DataGridTheme

@Composable
internal fun ColumnCell(
    text: String,
    index: Int,
    state: SnapshotStateMap<Int, Dp>,
    theme: DataGridTheme
) {
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
    Text(
        text = text,
        modifier, overflow = TextOverflow.Ellipsis,
        maxLines = 1, style = theme.columnCellTextStyle.textStyle,
        textAlign = TextAlign.Justify
    )
}