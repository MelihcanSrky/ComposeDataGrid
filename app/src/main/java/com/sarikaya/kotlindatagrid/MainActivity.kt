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
import com.sarikaya.kotlindatagrid.ui.theme.KotlinDataGridTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        setContent {
            var selectedRow by remember {
                mutableStateOf(-1)
            }

            var selectedCell by remember {
                mutableStateOf(-1)
            }

            var selectedRowData by remember {
                mutableStateOf(Products())
            }

            var selectedCellData by remember {
                mutableStateOf("")
            }

            var data by remember {
                mutableStateOf<List<Products>>(mutableListOf())
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
                        data = response.body()?.products ?: mutableListOf()
                        println(response.body()?.skip)
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {

                    }

                })
            }
            apiCall()
            var selectedRowsList by remember {
                mutableStateOf<List<Products>>(emptyList())
            }
            KotlinDataGridTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val dataGrid = DataGrid<Products>()
                            .setDataClass(Products::class)
                            .setCheckBoxColumn(true, multipleSelect = true)
                            .setColumn(ProductsColumn)
                            .setTableSize(height = 250.dp)
                            .setDataSource(data)
//                            .setPagination(8)
                            .setPagination(30, total.value) {
                                apiCall(it)
                            }
                            .setOnClickListeners {
//                                setOnRowClickListener { index, data ->
//                                    selectedRow = index
//                                    if (data != null) {
//                                        selectedRowData = data
//                                    }
//                                }
                                setOnRowListClickListener { index, list ->
                                    println("$index List: ${list?.size}")
                                    selectedRowsList = list!!
                                }
//                                setOnCellClickListener { row, cell, data ->
//                                    selectedRow = row
//                                    selectedCell = cell
//                                    selectedCellData = data
//                                }
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
    DataGridColumn("title", name = "İsim"),
    DataGridColumn("price", name = "Fiyat"),
    DataGridColumn("discountPercentage", name = "İndirim Yüzdesi"),
    DataGridColumn("rating", name = "Oylama"),
    DataGridColumn("brand", name = "Marka"),
    DataGridColumn("thumbnail", name = "Poster"),
)

val MockDataColumn = listOf(
    DataGridColumn("name", name = "Ad"),
    DataGridColumn("surname", name = "Soyad"),
    DataGridColumn("company", name = "Şirket"),
    DataGridColumn("city", name = "Şehir"),
    DataGridColumn("address", name = "Adres"),
    DataGridColumn("number", name = "Numara"),
)

data class MockDataClass(
    val name: String = "",
    val surname: String = "",
    val company: String = " ",
    val city: String = "",
    val address: String = " ",
    val number: String = ""
)

val MockData = listOf(
    MockDataClass(
        name = "Melih Can",
        surname = "Sarıkaya",
        city = "İzmir",
        company = "Netcad",
        number = "555 812 30 58",
        address = "Altındağ Yıldıztepe mah."
    ),
    MockDataClass(
        name = "Cafer",
        surname = "Sarıkaya",
        city = "Ankara",
        company = "Coldwell",
        number = "555 812 30 58",
        address = "Yıldıztepe mah."
    ),
    MockDataClass(
        name = "Durdane",
        surname = "Sarıkaya",
        city = "Ankara",
        company = "Ev",
        number = "555 812 30 58",
        address = "Altındağ Yıldıztepe"
    ),
    MockDataClass(
        name = "Funda",
        surname = "Sarıkaya",
        city = "Ankara",
        company = "Softtech",
        number = "",
        address = "Altındağ "
    ),
    MockDataClass(
        name = "Melih Can",
        surname = "Sarıkaya",
        city = "İzmir",
        company = "Netcad",
        number = "555 812 30 58",
        address = "Altındağ Yıldıztepe mah."
    ),
)