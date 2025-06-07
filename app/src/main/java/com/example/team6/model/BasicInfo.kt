package com.example.team6.model

data class BasicInfo(
    val kindercode: String?, // 1 유치원코드
    val officeedu: String?, // 2 교육청명
    val subofficeedu: String?, // 3 교육지원청명
    val kindername: String, // 4 유치원명 (비어있을 수 없으므로 String)
    val establish: String?, // 5 설립유형
    val rppnname: String?, // 6 대표자명
    val ldgrname: String?, // 7 원장명
    val edate: String?, // 8 설립일 (YYYYMMDD 형식 문자열)
    val odate: String?, // 9 개원일 (YYYYMMDD 형식 문자열)
    val addr: String, // 10 주소 (비어있을 수 없으므로 String)
    val telno: String?, // 11 전화번호
    val faxno: String?, // 12 FAX번호
    val hpaddr: String?, // 13 홈페이지 주소
    val opertime: String?, // 14 운영시간 (예: "09:00~17:00" 형식 문자열)
    val clcnt3: Int?, // 15 만3세학급수
    val clcnt4: Int?, // 16 만4세학급수
    val clcnt5: Int?, // 17 만5세학급수
    val mixclcnt: Int?, // 18 혼합학급수
    val shclcnt: Int?, // 19 특수학급수
    val prmstfcnt: Int?, // 20 인가총정원수
    val ag3fpcnt: Int?, // 21 만3세정원수
    val ag4fpcnt: Int?, // 22 만4세정원수
    val ag5fpcnt: Int?, // 23 만5세정원수 (원래 입력에 'ag4fpcnt'가 두 번 나와서 'ag5fpcnt'로 수정)
    val mixfpcnt: Int?, // 24 혼합정원수
    val spcnfpcnt: Int?, // 25 특수학급정원수
    val ppcnt3: Int?, // 26 만3세원아수
    val ppcnt4: Int?, // 27 만4세원아수
    val ppcnt5: Int?, // 28 만5세원아수
    val mixppcnt: Int?, // 29 혼합원아수
    val shppcnt: Int?, // 30 특수원아수
    val pbnttmng: String?, // 31 공시차수 (예: "20251" 형식 문자열)
    val rpstYn: String?, // 32 직무대리여부 ("Y" 또는 "N" 형식 문자열)
    val lttdcdnt: Double?, // 33 위도
    val lngtcdnt: Double? // 34 경도
)
