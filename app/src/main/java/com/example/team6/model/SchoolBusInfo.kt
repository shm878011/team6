package com.example.team6.model

data class SchoolBusInfo(
    val key: String,
    val kindercode: String, // 유치원 고유 코드
    val kindername: String, // 유치원명
    val schoolBusYn: String?, // 차량운영여부 (Y/N)
    val schoolBusCnt: String?, // 차량대수
    val schoolBusRideChildCnt: String?, // 등하원차량 탑승원아수
    val pbnttmng: String? // 공시연월
)