package com.example.team6.network

import com.example.team6.model.ClassAreaResponse
import com.example.team6.model.KindergartenResponse
import com.example.team6.model.SafeResponse
import com.example.team6.model.SchoolBusResponse
import com.example.team6.model.TeacherInfo
import com.example.team6.model.TeacherResponse
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

    @GET("api/notice/safetyEdu.do")
    suspend fun getKindergartenSafetyInfo(
        @Query("key") apiKey: String,
        @Query("sidoCode") sidoCode: String,
        @Query("sggCode") sggCode: String
    ): SafeResponse

    @GET("api/notice/teachersInfo.do")
    suspend fun getKindergartenTeachersInfo(
        @Query("key") apiKey: String,
        @Query("sidoCode") sidoCode: String,
        @Query("sggCode") sggCode: String,
    ): TeacherResponse

    @GET("api/notice/classArea.do")
    suspend fun getKindergartenclassArea(
        @Query("key") apiKey: String,
        @Query("sidoCode") sidoCode: String,
        @Query("sggCode") sggCode: String,
    ): ClassAreaResponse
}
