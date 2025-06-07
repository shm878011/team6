package com.example.team6.model

import com.google.gson.annotations.SerializedName

data class SafeResponse(
    val status: String,
    @SerializedName("kinderInfo") val safeInfo: List<SafeInfo>? // JSON의 "kinderInfo" 키에 해당하는 리스트
)