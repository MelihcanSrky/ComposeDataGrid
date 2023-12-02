package com.sarikaya.composedatagrid.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sarikaya.composedatagrid.model.DataGridThemeModel

internal class DataGridTheme {
    private var _instance: DataGridTheme? = null

    val instance: DataGridTheme
        get() {
            if (_instance == null) {
                _instance = DataGridTheme()
            }
            return _instance!!
        }

    var dataGridColors = Themes().defaultTheme
        private set

    var columnCellTextStyle = CellModifier(
        textStyle = TextStyle(
            fontSize = 12.sp,
            color = dataGridColors.columnLabelTextColor,
            fontWeight = FontWeight.SemiBold
        ),
    )
        private set

    var dataCellTextStyle = CellModifier(
        textStyle = TextStyle(
            fontSize = 12.sp,
            color = dataGridColors.dataLabelTextColor,
        ),
    )
        private set

    fun setDataGridColors(colors: DataGridThemeModel) {
        this.dataGridColors = colors
    }

    fun setColumnCellTextStyle(style: CellModifier) {
        this.columnCellTextStyle = style
    }

    fun setDataCellTextStyle(style: CellModifier) {
        this.dataCellTextStyle = style
    }
}

data class CellModifier(
    val textStyle: TextStyle,
    val padding: CellPadding = CellPadding(8.dp, 4.dp),
    val height: Dp? = null
)

data class CellPadding(
    val horizontal: Dp? = null,
    val vertical: Dp? = null
)