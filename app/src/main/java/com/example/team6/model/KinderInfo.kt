package com.example.team6.model

data class KinderInfo(
    val kindername: String, // 유치원명
    val addr: String, // 주소
    val telno: String?, // 전화번호
    val hpaddr: String?, // 홈페이지 주소
    val opertime: String?, // 운영시간 (시작시간~종료시간)
    val ldgrname: String?, // 원장명
    val latitude: Double?, //위도
    val longitude: Double?, //경도

    val totalCapacity: Int = 0, // 인가총정원수
    val current3yrOlds: Int = 0, // 만3세원아수
    val current4yrOlds: Int = 0, // 만4세원아수
    val current5yrOlds: Int = 0, // 만5세원아수
    val currentMixedOlds: Int = 0, // 혼합원아수
    val currentSpecialNeedsOlds: Int = 0 // 특수원아수
){
    val current: Int
        get() {
            return current3yrOlds + current4yrOlds + current5yrOlds + currentMixedOlds + currentSpecialNeedsOlds
        }
}