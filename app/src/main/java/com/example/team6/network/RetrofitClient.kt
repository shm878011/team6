package com.example.team6.network

import com.example.team6.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://maps.apigw.ntruss.com/"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_CLIENT_ID)
                    .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_CLIENT_SECRET)
                    .build()
                chain.proceed(request)
            }.build()
    }

    val geocodingApi: GeocodingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApi::class.java)
    }


    val reverseGeocodingApi: ReverseGeocodingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReverseGeocodingApi::class.java)
    }
}