package com.example.team6.model

data class KinderInfo(
    val key: String,
    val kindercode: String,
    val officeedu: String, // 교육청명
    val subofficeedu: String, // 교육지원청명
    val kindername: String, // 유치원명
    val establish: String?, // 설립유형
    val edate: String?, // 설립인가일 (yyyyMMdd)
    val odate: String?, // 개원일 (yyyyMMdd)
    val addr: String?, // 주소
    val telno: String?, // 전화번호
    val faxno: String?, // 팩스번호
    val hpaddr: String?, // 홈페이지 주소
    val opertime: String?, // 운영시간 (시작시간~종료시간)
    val clcnt3: String?, // 3세 학급수
    val clcnt4: String?, // 4세 학급수
    val clcnt5: String?, // 5세 학급수
    val mixclcnt: String?, // 혼합연령 학급수
    val shclcnt: String?, // 특수 학급수
    val ppcnt3: String?, // 3세 원아수
    val ppcnt4: String?, // 4세 원아수
    val ppcnt5: String?, // 5세 원아수
    val mixppcnt: String?, // 혼합연령 원아수
    val shppcnt: String?, // 특수 원아수
    val rppnname: String?, // 대표자명
    val ldgrname: String?, // 원장명
    val pbnttmng: String?, // 공시연월
    val sidoCode: String?, //시도코드
    val sggCode: String?  // 시군구코드
)