package com.sarikaya.composedatagrid.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sarikaya.composedatagrid.model.DataGridThemeModel

internal fun Modifier.setClickable(onClick: (() -> Unit)?) = this.clickable(
    enabled = onClick != null
) {
    if (onClick != null) {
        onClick()
    }
}

internal fun Modifier.setBorder(color: Color?) = this.border((0.5).dp, color?: Color.Gray)

internal fun Modifier.setPadding() = this.padding(8.dp, 4.dp)

internal fun Modifier.setWidth(width: Dp?) = if (width != null) this.width(width) else this
internal fun Modifier.setHeight(height: Dp? = null) =
    if (height != null) this.height(height) else this.height(24.dp)

internal fun Modifier.setConditionalColor(p0: Boolean, color: Color) = run {
    if (p0) {
        this.background(color)
    } else {
        this.background(Color.Transparent)
    }
}

internal fun Modifier.setCellColor(p0: Boolean, p1: Boolean, theme: DataGridThemeModel) = run {
    if (p0 && p1) {
        this.background(theme.selectedCellColor())
    } else if (p0 || p1) {
        this.background(theme.selectedRowColor())
    } else {
        this.background(Color.Transparent)
    }
}

internal fun Modifier.setTableWidth(width: Dp?) =
    if (width != null) this.width(width) else this.fillMaxWidth()

internal fun Modifier.setTableHeight(height: Dp?) =
    if (height != null) this.height(height - 48.dp) else this