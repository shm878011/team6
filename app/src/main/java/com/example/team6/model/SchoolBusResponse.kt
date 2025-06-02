package com.example.team6.model

data class SchoolBusResponse(
    val status: String,
    val schoolBusInfo: List<SchoolBusInfo> // 통학차량 정보 리스트
)