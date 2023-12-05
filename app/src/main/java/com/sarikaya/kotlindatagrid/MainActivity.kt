package com.sarikaya.kotlindatagrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sarikaya.composedatagrid.DataGrid
import com.sarikaya.composedatagrid.model.DataGridColumn
import com.sarikaya.composedatagrid.utils.ColumnType
import com.sarikaya.kotlindatagrid.ui.theme.KotlinDataGridTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val mockData = mutableListOf<MockDataClass>()
        for (i in 0..50) {
            mockData.add(
                MockDataClass(
                    "Melih Can",
                    "Sarıkaya",
                    i,
                    "ankara",
                    i * ((i - 1) * 100),
                )
            )
        }

        setContent {
            var selectedRow by remember {
                mutableStateOf(-1)
            }

            var selectedCell by remember {
                mutableStateOf(-1)
            }

            var selectedRowData by remember {
                mutableStateOf(MockDataClass())
            }

            var selectedCellData by remember {
                mutableStateOf("")
            }

            var data by remember {
                mutableStateOf<List<MockDataClass>>(mutableListOf())
            }

            val page = remember {
                mutableStateOf(0)
            }

            val total = remember {
                mutableStateOf(0)
            }

            fun apiCall(page: Int = 0) {
                val call = apiService.getData(page * 30)
                call.enqueue(object : Callback<Response> {
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        total.value = response.body()?.total!!
//                        data = response.body()?.products ?: mutableListOf()
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {

                    }

                })
            }
            apiCall()
            var selectedRowsList by remember {
                mutableStateOf<List<MockDataClass>>(emptyList())
            }
            KotlinDataGridTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {

                        val dataGrid = DataGrid<MockDataClass>()
                            .setDataClass(MockDataClass::class)
                            .setCheckBoxColumn(true, multipleSelect = true)
                            .setColumn(MockDataColumn)
                            .setTableSize(height = 250.dp)
                            .setDataSource(mockData)
                            .setSorting(true)
                            .setPagination(8)
//                            .setPagination(30, total.value) {
//                                apiCall(it)
//                            }
                            .setOnClickListeners {
                                setOnRowListClickListener { index, list ->
                                    selectedRowsList = list!!
                                }
//                                setOnCellClickListener { row, cell, data ->
//                                    selectedRow = row
//                                    selectedCell = cell
//                                    selectedCellData = data
//                                }
                                setOnRowClickListener { index, data ->
                                    selectedRow = index
                                    if (data != null) {
                                        selectedRowData = data
                                    }
                                }
                            }

                        dataGrid.build()
                        Text(text = "Selected Row: $selectedRow \nSelected Cell: $selectedCell")
                        Text(text = "Selected Data: $selectedRowData \nSelected Cell Data: $selectedCellData")
                        Text(text = "Selected RowsList: ${selectedRowsList}")
                    }
                }
            }
        }
    }
}

val ProductsColumn = listOf(
    DataGridColumn("id", name = "ID"),
    DataGridColumn("title", name = "Name"),
    DataGridColumn("price", name = "Price"),
    DataGridColumn("discountPercentage", name = "Discount Percentage"),
    DataGridColumn("rating", name = "Rating"),
    DataGridColumn("brand", name = "Brand"),
    DataGridColumn("thumbnail", name = "Thumbnail", defaultVisibility = false),
)

val MockDataColumn = listOf(
    DataGridColumn("name", name = "Ad"),
    DataGridColumn("surname", name = "Soyad"),
    DataGridColumn("company", name = "Şirket", type = ColumnType.NUMBER),
    DataGridColumn("city", name = "Şehir"),
    DataGridColumn("address", name = "Adres", type = ColumnType.NUMBER),
    DataGridColumn("number", name = "Numara"),
)

data class MockDataClass(
    val name: String = "",
    val surname: String = "",
    val company: Int = 0,
    val city: String = "",
    val address: Int = 0,
    val number: String = ""
)

