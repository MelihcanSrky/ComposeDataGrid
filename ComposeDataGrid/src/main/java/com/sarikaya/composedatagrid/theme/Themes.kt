package com.sarikaya.composedatagrid.theme

import androidx.compose.ui.graphics.Color
import com.sarikaya.composedatagrid.model.DataGridCheckBoxIconColor
import com.sarikaya.composedatagrid.model.DataGridPaginationThemeModel
import com.sarikaya.composedatagrid.model.DataGridThemeModel

class Themes {
    val defaultTheme = DataGridThemeModel(
        primary = Color(0xFF146C94),
        secondary = Color(0xFFAFD3E2),
        columnLabelTextColor = Color.White,
        dataLabelTextColor = Color.Gray,
        backgroundColor = Color.White,
        headerRowColor = Color(0xFFAFD3E2),
        borderColor = Color(0xFF146C94),
        paginationTheme = DataGridPaginationThemeModel(
            paginationUnitBackground = Color(0xFFAFD3E2),
            paginationIconButtonColor = Color.White,
            paginationLabelColor = Color.White,
            paginationBorderColor = Color(0xFF146C94)
        ),
        checkBoxTheme = DataGridCheckBoxIconColor(
            selectedIconBackground = Color(0xFFAFD3E2),
            selectedIconColor = Color.White,
            unSelectedIconColor = Color(0xFF146C94),
            iconBorderColor = Color(0xFFAFD3E2)
        )
    )
}