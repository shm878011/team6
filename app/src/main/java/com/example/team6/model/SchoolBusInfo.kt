package com.example.team6.model

data class SchoolBusInfo(
    val key: String,
    val kindercode: String,
    val officeedu: String,
    val subofficeedu: String,
    val kindername: String,        // 유치원명
    val establish: String?,
    val vhcl_oprn_yn: String?,     // 추가: 차량운영여부 (Y/N)
    val opra_vhcnt: String?,       // 추가: 운행차량수
    val dclr_vhcnt: String?,
    val psg9_dclr_vhcnt: String?,
    val psg12_dclr_vhcnt: String?,
    val psg15_dclr_vhcnt: String?,
    val pbnttmng: String?,
    val page: String?
)