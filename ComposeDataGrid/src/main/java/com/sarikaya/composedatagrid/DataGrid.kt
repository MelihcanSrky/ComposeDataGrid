package com.sarikaya.composedatagrid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sarikaya.composedatagrid.components.ColumnGridRow
import com.sarikaya.composedatagrid.components.DataGridRow
import com.sarikaya.composedatagrid.extensions.filterForIndexList
import com.sarikaya.composedatagrid.extensions.getPageCount
import com.sarikaya.composedatagrid.extensions.setBorder
import com.sarikaya.composedatagrid.extensions.setPagination
import com.sarikaya.composedatagrid.extensions.setTableHeight
import com.sarikaya.composedatagrid.extensions.setTableWidth
import com.sarikaya.composedatagrid.gridindexselector.OnClickListenerEvent
import com.sarikaya.composedatagrid.gridindexselector.OnClickListenerImpl
import com.sarikaya.composedatagrid.model.DataGridColumn
import com.sarikaya.composedatagrid.model.DataGridThemeModel
import com.sarikaya.composedatagrid.theme.CellModifier
import com.sarikaya.composedatagrid.theme.CellPadding
import com.sarikaya.composedatagrid.theme.DataGridTheme
import com.sarikaya.composedatagrid.theme.Themes
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class DataGrid<D : Any> {
    // DataGrid Columns.
    private var columns: MutableMap<Int, DataGridColumn> = mutableMapOf()

    // DataGrid data sources from user. (maybe async)
    private var dataSource: MutableMap<Int, D> = mutableMapOf()

    // DataGrid CheckBox columns for start of grid to multiple choices.
    private var checkBoxColumn: Boolean = false
    private var multipleSelect: Boolean = false

    // DataGrid data class for declare class that user wants to use.
    private lateinit var dataClass: KClass<D>

    // DataGrid on click listener implementation for data grid clickables.
    private val onClickListener = OnClickListenerImpl<D>()

    // DataGrid Theme for different variations.
    private val theme = DataGridTheme()

    // DataGrid Width. fillWidth if null
    private var width: Dp? = null

    // DataGrid Height. wrapHeight if null
    private var height: Dp? = null

    // DataGrid Pagination Operation Variables
    private var isPaginationEnabled: Boolean = false
    private var isPaginationAsync: Boolean = false
    private var onAsyncPageChangeListener: ((Int) -> Unit)? = null
    private var pagingLimit: Int? = null
    private var pagingTotal: Int? = null

    fun setTableSize(width: Dp? = null, height: Dp? = null): DataGrid<D> {
        this.width = width
        this.height = height
        return this
    }

    fun setDataGridColors(colors: DataGridThemeModel): DataGrid<D> {
        this.theme.setDataGridColors(colors)
        return this
    }

    fun setCheckBoxColumn(check: Boolean, multipleSelect: Boolean = false): DataGrid<D> {
        this.checkBoxColumn = check
        this.multipleSelect = multipleSelect
        return this
    }

    /**
     * DataGrid library setDataClass function.
     *
     * It takes a data class of the data type to be used for the DataGrid to operate.
     *
     * @param [dataClass] gets a any type of data class.
     *
     * For example ExampleModel::class
     */
    fun setDataClass(dataClass: KClass<D>): DataGrid<D> {
        this.dataClass = dataClass
        return this
    }

    /**
     * DataGrid library setColumn function.
     *
     * In order for DataGrid to operate, it receives a list of
     * [DataGridColumn] type for the data to be displayed in the table.
     * @param[columns] gets a list of [DataGridColumn].
     */
    fun setColumn(columns: List<DataGridColumn>): DataGrid<D> {
        this.columns.clear()
        columns.forEachIndexed { index, column ->
            this.columns.put(index, column)
        }
        return this
    }

    /**
     * DataGrid library setDataSource function.
     *
     * For the data to be displayed in the DataGrid,
     * the list of data sources available to the user must be entered.
     * @param[dataSource] gets a list of [Any].
     */
    fun setDataSource(dataSource: List<D>): DataGrid<D> {
        this.dataSource.clear()
        dataSource.forEachIndexed { index, any ->
            this.dataSource.put(index, any)
        }
        return this
    }

    /**
     * DataGrid setPagination function.
     *
     * @param [limit] Gets a page limit number of type [Integer].
     *
     * It can be used for large data or non-pagination service requests.
     */
    fun setPagination(limit: Int): DataGrid<D> {
        this.pagingLimit = limit
        this.isPaginationEnabled = true
        return this
    }

    /**
     * DataGrid setPagination function.
     *
     * @param [limit] Gets a page limit number of type [Integer].
     * @param [total] Gets the total number of data of type [Integer] if specified as incoming optional.
     * @param [onAsyncPageChangeListener] For pagination dependent data pulled asynchronously,
     * it allows new data to be retrieved when the page is changed.
     * Returns the page number in [Integer] type.
     */
    fun setPagination(
        limit: Int, total: Int? = null, onAsyncPageChangeListener: (Int) -> Unit
    ): DataGrid<D> {
        this.pagingLimit = limit
        this.pagingTotal = total
        this.onAsyncPageChangeListener = onAsyncPageChangeListener
        this.isPaginationEnabled = true
        this.isPaginationAsync = true
        return this
    }

    /**
     * DataGrid library setOnClickListeners function.
     *
     * @param[onClickListeners] It can get selected functions from the [OnClickListenerEvent].
     */
    fun setOnClickListeners(onClickListeners: OnClickListenerEvent<D>.() -> Unit): DataGrid<D> {
        onClickListener.onClickListeners()
        return this
    }

    @Composable
    fun build() {
        val props = dataClass.declaredMemberProperties
        val columnWidths = remember {
            mutableStateMapOf<Int, Dp>()
        }
        val selectedRowIndexes = remember {
            mutableStateOf(mutableListOf<Int>())
        }
        val rowIndex = remember {
            mutableStateOf(-1)
        }
        val cellIndex = remember {
            mutableStateOf(-1)
        }
        val page = remember {
            mutableStateOf(0)
        }
        val isLoading = remember {
            mutableStateOf(false)
        }
        Column {
            if (dataSource.isNotEmpty() && !isLoading.value) {
                LazyRow(
                    Modifier
                        .background(theme.dataGridColors.backgroundColor)
                        .setTableWidth(width)
                ) {
                    item {
                        Column(
                            Modifier.setTableHeight(height)
                        ) {
                            ColumnGridRow(
                                columns = columns,
                                columnWidths = columnWidths,
                                theme = theme,
                                checkBoxEnabled = checkBoxColumn,
                                checkBoxState = selectedRowIndexes.value.isNotEmpty()
                            ) {
                                selectedRowIndexes.value = mutableListOf()
                                if (!onClickListener.isRowListNull()) {
                                    onClickListener.onRowClicked(-1)
                                } else {
                                    onClickListener.onRowClicked(-1)
                                }
                            }
                            LazyColumn {
                                itemsIndexed(
                                    dataSource.entries.toList().setPagination(
                                        isPaginationEnabled,
                                        isPaginationAsync,
                                        pagingLimit,
                                        page,
                                    )
                                ) { index, data ->
                                    DataGridRow(
                                        rowIndex = rowIndex,
                                        cellIndex = cellIndex,
                                        index = index,
                                        data = data,
                                        props = props,
                                        columnWidths = columnWidths,
                                        onClickSelector = onClickListener,
                                        theme = theme,
                                        columns = columns,
                                        checkBoxEnabled = checkBoxColumn,
                                        selectedIndexList = selectedRowIndexes.value
                                    ) { bool ->
                                        rowIndex.value = index
                                        if (multipleSelect) {
                                            if (selectedRowIndexes.value.contains(index)) {
                                                selectedRowIndexes.value.remove(index)
                                            } else {
                                                selectedRowIndexes.value.add(index)
                                            }
                                        } else {
                                            onClickListener.onRowClicked(-1)
                                            if (selectedRowIndexes.value.any { it == index }) {
                                                selectedRowIndexes.value = mutableListOf()
                                            } else {
                                                selectedRowIndexes.value = mutableListOf()
                                                selectedRowIndexes.value.add(0, index)
                                            }
                                        }
                                        if (!onClickListener.isRowListNull()) {
                                            onClickListener.onRowClicked(
                                                index,
                                                list = dataSource.filterForIndexList(
                                                    selectedRowIndexes.value
                                                )
                                            )
                                        } else {
                                            onClickListener.onRowClicked(index, data.value)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    Modifier
                        .background(theme.dataGridColors.backgroundColor)
                        .setTableWidth(width)
                        .setTableHeight(height),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = theme.dataGridColors.primary)
                }
            }
            if (isPaginationEnabled) {
                PaginationControlUnit(page = page, isLoading = isLoading)
            }
        }
    }

    @Composable
    private fun PaginationControlUnit(
        page: MutableState<Int>,
        isLoading: MutableState<Boolean>
    ) {
        Row(
            Modifier
                .height(48.dp)
                .background(theme.dataGridColors.paginationTheme.paginationUnitBackground)
                .setBorder(theme.dataGridColors.paginationTheme.paginationBorderColor)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (page.value > 0) {
                    page.value--
                    if (isPaginationAsync) {
                        isLoading.value = true
                        dataSource.clear()
                        onAsyncPageChangeListener?.let { it(page.value) }
                        isLoading.value = false
                    }
                }
            }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft, contentDescription = "",
                    tint = theme.dataGridColors.paginationTheme.paginationIconButtonColor
                )
            }
            Text(
                text = if (isPaginationAsync) "${page.value + 1} / ${
                    pagingTotal?.getPageCount(
                        pagingLimit!!
                    ) ?: ""
                }" else "${page.value + 1} / ${dataSource.getPageCount(pagingLimit!!)}",
                color = theme.dataGridColors.paginationTheme.paginationLabelColor,
                style = theme.dataCellTextStyle.textStyle
            )
            IconButton(onClick = {
                if (isPaginationAsync) {
                    if (pagingTotal != null) {
                        if (page.value + 1 < (pagingTotal!!.getPageCount(
                                pagingLimit!!
                            ))
                        ) {
                            isLoading.value = true
                            page.value++
                            dataSource.clear()
                            onAsyncPageChangeListener?.let { it(page.value) }
                            isLoading.value = false
                        }
                    } else {
                        if (dataSource.size == pagingLimit!!) {
                            isLoading.value = true
                            page.value++
                            dataSource.clear()
                            onAsyncPageChangeListener?.let { it(page.value) }
                            isLoading.value = false
                        }
                    }
                } else {
                    if (page.value + 1 < (dataSource.getPageCount(pagingLimit!!))) {
                        page.value++
                    }
                }
            }) {
                Icon(
                    Icons.Default.KeyboardArrowRight, contentDescription = "",
                    tint = theme.dataGridColors.paginationTheme.paginationIconButtonColor
                )
            }
        }
    }
}