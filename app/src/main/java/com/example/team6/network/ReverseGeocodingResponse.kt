package com.example.team6.network

data class ReverseGeocodingResponse(
    val results: List<Result>
)

data class Result(
    val name: String,
    val region: Region
)

data class Region(
    val area1: Area,
    val area2: Area,
    val area3: Area
)

data class Area(
    val name: String
)