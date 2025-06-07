package com.example.team6.network

data class GeocodingResponse(
    val status: String,
    val addresses: List<Address>
)

data class Address(
    val roadAddress: String?,
    val jibunAddress: String?,
    val englishAddress: String?,
    val x: String?,
    val y: String?
)