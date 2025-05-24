package com.example.team6.model

data class Nursery(
    val name: String,
    val rating: Double,
    val address: String,
    val phone: String,
    val cctvCount: Int,
    val roomCount: Int,
    val playgroundCount: Int,
    val capacity: Int,
    val current: Int,
    val staffCount: Int,
    val hasBus: String,
    val reviewCount: Int
)

val dummyNurseries = listOf(
    Nursery("자양어린이집", 4.8, "서울 광진구 자양로 35", "02-123-4567", 8, 6, 1, 80, 63, 6, "Y", 2),
    Nursery("능동어린이집", 4.3, "서울 광진구 능동로 476", "02-345-6789", 5, 5, 0, 60, 55, 5, "N", 0),
    Nursery("푸른어린이집", 4.5, "서울 광진구 자양변영로 35", "02-888-8888", 10, 7, 2, 90, 70, 7, "Y", 3)
)