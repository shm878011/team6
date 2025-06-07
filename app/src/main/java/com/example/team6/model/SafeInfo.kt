package com.example.team6.model

import com.google.gson.annotations.SerializedName

data class SafeInfo(
    val key: String?,
    val page: String?,
    val kindercode: String,
    val officeedu: String,
    val subofficeedu: String,
    val kindername: String,
    val estb_pt: String,

    val plyg_ck_yn: String, // 놀이시설 안전점검 실시 여부 (Y/N)
    val plyg_ck_dt: String?, // 놀이시설 안전점검 일자 (YYYYMMDD)
    val plyg_ck_rs_cd: String, // 놀이시설 안전점검 결과 코드 (예: 적합, 부적합, NA)

    val cctv_ist_yn: String, // CCTV 설치 여부 (Y/N)
    val cctv_ist_total: String, // CCTV 총 대수
    val cctv_ist_in: String, // CCTV 실내 대수
    val cctv_ist_out: String, // CCTV 실외 대수

    val fire_avd_yn: String, // 소방훈련 실시 여부 (Y/N)
    val fire_avd_dt: String?, // 소방훈련 일자 (YYYYMMDD)
    val fire_safe_yn: String, // 소방안전점검 실시 여부 (Y/N)
    val fire_safe_dt: String?, // 소방안전점검 일자 (YYYYMMDD)

    val gas_ck_yn: String, // 가스 안전점검 실시 여부 (Y/N)
    val gas_ck_dt: String?, // 가스 안전점검 일자 (YYYYMMDD)

    val elect_ck_yn: String, // 전기 안전점검 실시 여부 (Y/N)
    val elect_ck_dt: String?, // 전기 안전점검 일자 (YYYYMMDD)

    val pbnttmng: String // 공고 시점
)
