package com.example.team6.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReverseGeocodingApi {
    @GET("map-reversegeocode/v2/gc")
    suspend fun getAddress(
        @Query("coords") coords: String,
        @Query("orders") orders: String = "legalcode,admcode,addr",
        @Query("output") output: String = "json"
    ): Response<ReverseGeocodingResponse>
}