package com.example.team6.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GeocodingApi {
    @GET("map-geocode/v2/geocode")
    suspend fun getGeocode(
        @Query("query") query: String
    ): Response<GeocodingResponse>
}