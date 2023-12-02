package com.sarikaya.composedatagrid.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
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
internal fun DataCell(
    text: String,
    index: Int,
    state: SnapshotStateMap<Int, Dp>,
    modifier: Modifier = Modifier,
    theme: DataGridTheme
) {
    val localDensity = LocalDensity.current
    val _modifier = modifier
        .onGloballyPositioned { coor ->
            state[index].let {
                if (it != null) {
                    if (it <= with(localDensity) { coor.size.width.toDp() }) {
                        state[index] = with(localDensity) { coor.size.width.toDp() }
                    }
                }
            }
        }
        .then(
            other = Modifier
                .setWidth(state[index])
                .setHeight(theme.dataCellTextStyle.height)
                .setBorder(theme.dataGridColors.borderColor)
                .setPadding()
        )
    Text(
        text = text,
        _modifier, overflow = TextOverflow.Ellipsis,
        maxLines = 1, style = theme.dataCellTextStyle.textStyle
    )
}