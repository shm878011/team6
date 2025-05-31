package com.example.team6.network

import com.example.team6.model.KindergartenResponse
import com.example.team6.model.SchoolBusResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface KindergartenApiService {
    @GET("api/notice/basicInfo.do")
    suspend fun getKindergartenBasicInfo(
        @Query("key") apiKey: String,
        @Query("sidoCode") sidoCode: String,
        @Query("sggCode") sggCode: String
    ): KindergartenResponse

    @GET("api/notice/schoolBus.do")
    suspend fun getKindergartenSchoolBusInfo(
        @Query("key") apiKey: String,
        @Query("sidoCode") sidoCode: String,
        @Query("sggCode") sggCode: String
    ): SchoolBusResponse
}
