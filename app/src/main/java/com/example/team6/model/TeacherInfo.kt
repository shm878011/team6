package com.example.team6.model

data class TeacherInfo(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,

    // 교직원 현황 수
    val drcnt: Int?, // 원장수
    val adcnt: Int?, // 원감수
    val hdst_thcnt: Int?, // 수석교사수
    val asps_thcnt: Int?, // 보직교사수
    val gnrl_thcnt: Int?, // 일반교사수
    val spcn_thcnt: Int?, // 특수교사수
    val ntcnt: Int?, // 보건교사수 (API 문서상 영양사수일 가능성도 있으니 확인 필요)
    val ntrt_thcnt: Int?, // 영양교사수 (API 문서상 간호사수일 가능성도 있으니 확인 필요)
    val shcnt_thcnt: Int?, // 기간제교사/강사수
    val owcnt: Int?, // 사무직원수

    // 교원 자격증 소지 현황 수
    val hdst_tchr_qacnt: Int?, // 수석교사자격수
    val rgth_gd1_qacnt: Int?, // 정교사1급자격수
    val rgth_gd2_qacnt: Int?, // 정교사2급자격수
    val asth_qacnt: Int?, // 준교사자격수
    val spsc_tchr_qacnt: Int?, // 특수학교정교사자격수
    val nth_qacnt: Int?, // 보건교사자격수
    val ntth_qacnt: Int?, // 영양교사자격수

    val pbntTmng: String? // 공시차수
)
