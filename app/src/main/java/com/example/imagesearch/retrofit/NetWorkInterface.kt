package com.example.imagesearch.retrofit

import com.example.imagesearch.data.Image
import retrofit2.http.GET
import retrofit2.http.Query

interface NetWorkInterface {
    @GET("v2/search/image")
    suspend fun getImage(
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Image
}