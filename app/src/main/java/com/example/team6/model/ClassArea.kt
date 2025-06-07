package com.example.team6.model

data class ClassArea(
    val kindercode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,

    // 교실수 및 면적 관련 필드
    val crcnt: String?, // 교실수 (예: "8개")
    val clsrarea: String?, // 교실면적 (예: "663㎡")
    val phgrindrarea: String?, // 체육장 (예: "0㎡")
    val hlsparea: String?, // 보건/위생공간 (예: "60㎡")
    val ktchmssparea: String?, // 조리실/급식공간 (예: "23㎡")
    val otsparea: String?, // 기타공간 (예: "243㎡")

    val pbntTmng: String? // 공시차수 (예: "20251")
)
