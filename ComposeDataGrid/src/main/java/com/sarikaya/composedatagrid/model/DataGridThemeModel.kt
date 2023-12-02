package com.sarikaya.composedatagrid.model

import androidx.compose.ui.graphics.Color

data class DataGridThemeModel(
    val primary: Color,
    val secondary: Color,
    val backgroundColor: Color,
    val columnLabelTextColor: Color,
    val dataLabelTextColor: Color,
    val headerRowColor: Color,
    val borderColor: Color,
    val paginationTheme: DataGridPaginationThemeModel,
    val checkBoxTheme: DataGridCheckBoxIconColor
) {
    fun selectedRowColor() : Color {
        return makeColorDarker(backgroundColor, 0.1f)
    }

    fun selectedCellColor() : Color {
        return makeColorDarker(backgroundColor, 0.25f)
    }

    private fun makeColorDarker(color: Color, percentage: Float) : Color {
        val red = (color.red * (1 - percentage)).coerceIn(0f, 1f)
        val green = (color.green * (1 - percentage)).coerceIn(0f, 1f)
        val blue = (color.blue * (1 - percentage)).coerceIn(0f, 1f)

        return Color(red, green, blue, color.alpha)
    }
}

data class DataGridPaginationThemeModel(
    val paginationUnitBackground: Color,
    val paginationIconButtonColor: Color,
    val paginationLabelColor: Color,
    val paginationBorderColor: Color
)

data class DataGridCheckBoxIconColor(
    val selectedIconBackground: Color,
    val selectedIconColor: Color,
    val unSelectedIconColor: Color,
    val iconBorderColor: Color
)