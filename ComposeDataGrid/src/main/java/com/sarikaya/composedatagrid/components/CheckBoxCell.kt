package com.sarikaya.composedatagrid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sarikaya.composedatagrid.extensions.setBorder
import com.sarikaya.composedatagrid.extensions.setClickable
import com.sarikaya.composedatagrid.extensions.setHeight
import com.sarikaya.composedatagrid.theme.DataGridTheme

@Composable
internal fun CheckBoxColumn(
    checked: Boolean,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    clearAllCheckBox: Boolean = false,
    theme: DataGridTheme
) {
    val modifier = modifier
        .setBorder(theme.dataGridColors.borderColor)
        .setHeight(if (clearAllCheckBox) theme.columnCellTextStyle.height else null)
        .width(24.dp)
        .padding(4.dp)
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        if (clearAllCheckBox) {
            Icon(
                imageVector = if (!checked) Icons.Default.Check else Icons.Default.Close,
                contentDescription = "",
                tint = if (checked) theme.dataGridColors.checkBoxTheme.selectedIconColor else Color.Transparent,
                modifier = Modifier
                    .setClickable {
                        if (onClick != null) {
                            onClick()
                        }
                    }
                    .size(12.dp)
                    .setBorder(theme.dataGridColors.borderColor)
                    .background(if (checked) theme.dataGridColors.checkBoxTheme.selectedIconBackground else Color.Transparent)
            )
        } else {
            Icon(
                imageVector = if (!checked) Icons.Default.Check else Icons.Default.Close,
                contentDescription = "",
                tint = if (checked) theme.dataGridColors.checkBoxTheme.selectedIconColor else theme.dataGridColors.checkBoxTheme.unSelectedIconColor,
                modifier = Modifier
                    .size(12.dp)
                    .setBorder(theme.dataGridColors.borderColor)
                    .background(if (checked) theme.dataGridColors.checkBoxTheme.selectedIconBackground else Color.Transparent)
            )

        }
    }
}