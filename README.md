# ComposeDataGrid

[![](https://jitpack.io/v/MelihcanSrky/ComposeDataGrid.svg)](https://jitpack.io/#MelihcanSrky/ComposeDataGrid)

Gradle: 8.0

Platforms: Android

![image](https://github.com/MelihcanSrky/ComposeDataGrid/assets/62643822/cd48e28e-9092-469d-acba-ac69c15073a8)


## Download & Quick Start
1. Add it in your settings.gradle.kts file:
```kotlin
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```

2. Add the dependency in your build.gradle.kts
```kotlin
dependencies {
	implementation("com.github.MelihcanSrky:KotlinCustomComponents:0.1.12")
}
```

## Usage
### Required functions to be added
```kotlin
val dataGrid = DataGrid<Products>()
    .setDataClass(Products::class) 
    .setColumn(ProductsColumn) 
    .setDataSource(data)

dataGrid.build()
```
DataGrid takes the data class of the data to be displayed in the table as its dynamic type. In the example we used:
```kotlin
data class Products(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("discountPercentage") var discountPercentage: Double? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("stock") var stock: Int? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
)
```
.setDataClass(): The function takes the class of the data data class. In this example it takes the Products::class variable as above.

.setColumn(): The function is given a list of type DataGridColumn that comes with the library. This allows you to set which fields of the data you want to display in the table. 
In this example, a list like the one below is created:
```kotlin
val ProductsColumn = listOf(
    DataGridColumn("id", name = "ID"),
    DataGridColumn("title", name = "Name"),
    DataGridColumn("price", name = "Price"),
    DataGridColumn("discountPercentage", name = "Discount Percentage"),
    DataGridColumn("brand", name = "Brand", defaultVisibility = false),
)
```

.setDataSource(): Data takes a list of type data class. This data can be asynchronous or static list.
In this example, asynchronous data is used. Therefore the variable "data" comes from the retrofit source.

.build() The Composable function is called to create the data table on the screen.

------------------------------------------------------------

### Optional functions that can be added

.setCheckBoxColumn(): When the function is activated, a check box column is added at the top of the table. There is also a variable multipleSelect, although it is not required. 
When this is activated, the user is given the ability to select more than one row from the table (multipleSelect is only available when the check box column is activated).

.setTableSize(): Function can take width and height values, but not necessarily. When the function is not used, it takes max width and content height values.

### Pagination
There are two variants. The first one can be used for static or asynchronous data without pagination. 
The second one is used in remote data with pagination to perform operations such as page changes in page transitions.

.setPagination(): function takes a "limit" parameter of type Int.

.setPagination(): function takes a "limit" parameter of type Int. If there is a total number of data in the incoming request, it can take a "total" parameter of type Int. 
It takes an "onAsyncPageChangeListener" function of type Lambda function. In this example it is used as follows:
```kotlin
.setPagination(30, total.value) {
    apiCall(it)
}
```

### On Click Listeners
The lamda function can take one of three clickListener depending on the intended use of the table.

1- setOnRowClickListener { }: According to the function to be written in it, the index of the last clicked row and the data in the row can be used (if multipleSelect is on, it takes the last clicked data). For example:
```kotlin
.setOnClickListeners {
  setOnRowClickListener { index, data ->
      selectedRow = index
      if (data != null) {
          selectedRowData = data
      }
  }
}
```

2- setOnRowClickListener { }: Lists the data of all selected rows in the order of selection according to the function to be written in it. For example:
```kotlin
.setOnClickListeners {
  setOnRowListClickListener { index, list ->
    println("$index List: ${list?.size}")
    selectedRowsList = list!!
  }
}
```
3- setOnCellClickListener { }: The row, column and data values of the selected box can be used according to the function to be written in it. For example:
```kotlin
.setOnClickListeners {
  setOnCellClickListener { row, cell, data ->
    selectedRow = row
    selectedCell = cell
    selectedCellData = data
  }
}
```
NOT: If setOnCellClickListener function is written, cell based selection is made. If setOnRowListClickListener function is written, list based selection is made. 
If setOnRowClickListener function is written, single selection is made. In this order, cell based selection is made if All is written, list based selection is made if list and single selection is written.

## Theming
In development

Right now it's just a color change.

.setDataGridColors(): Takes a variable of type DataGridThemeModel(). For example:
```kotlin
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
```

## Example Usage
```kotlin
val dataGrid = DataGrid<Products>()
    .setDataClass(Products::class)
    .setCheckBoxColumn(true, multipleSelect = false)
    .setColumn(ProductsColumn)
    .setTableSize(height = 250.dp)
    .setDataSource(data)
    .setPagination(30, total.value) {
        apiCall(it)
    }
    .setOnClickListeners {
        setOnRowClickListener { index, data ->
            selectedRow = index
            if (data != null) {
                selectedRowData = data
            }
        }
    }

dataGrid.build()

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

```

```kotlin
val ProductsColumn = listOf(
    DataGridColumn("id", name = "ID"),
    DataGridColumn("title", name = "Name"),
    DataGridColumn("price", name = "Price"),
    DataGridColumn("discountPercentage", name = "Discount Percentage"),
    DataGridColumn("rating", name = "Rating"),
    DataGridColumn("brand", name = "Brand"),
    DataGridColumn("thumbnail", name = "Thumbnail"),
)

data class Response(
    @SerializedName("products") var products: ArrayList<Products> = arrayListOf(),
    @SerializedName("total") var total: Int? = null,
    @SerializedName("skip") var skip: Int? = null,
    @SerializedName("limit") var limit: Int? = null
)

data class Products(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("discountPercentage") var discountPercentage: Double? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("stock") var stock: Int? = null,
    @SerializedName("brand") var brand: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
)
```
