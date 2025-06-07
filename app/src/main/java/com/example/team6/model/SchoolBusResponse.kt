package com.example.team6.model

import com.google.gson.annotations.SerializedName

data class SchoolBusResponse(
    val status: String,
    @SerializedName("kinderInfo") // <-- 이 줄을 추가하여 JSON의 "kinderInfo"와 매핑합니다.
    val schoolBusInfo: List<SchoolBusInfo>?
)