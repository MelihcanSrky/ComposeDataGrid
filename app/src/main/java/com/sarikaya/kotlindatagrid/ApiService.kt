package com.sarikaya.kotlindatagrid

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    fun getData(
        @Query("skip") skip: Int
    ): Call<Response>
}