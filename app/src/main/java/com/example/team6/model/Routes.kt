package com.example.team6.model

import okhttp3.Route

sealed class Routes(val route: String) {
    object Login : Routes("login") // 로그인화면
    object Signup : Routes("signup") // 회원가입
    object Home : Routes("home") // 첫 화면
    object AccountInfo : Routes("account_info") // 내 정보
    object MyReviews : Routes("my_reviews") // 내 리뷰
    object FavoriteNurseries : Routes("favorite_nurseries") // 어린이집 추천
    object LocationSetting : Routes("location_setting") // 위치 설정

    object ResultWithArgs : Routes(
        "result/{age}/{importantPoint}/{guardianAvailable}/{active}/{nowadmission}"
    ) {
        fun createRoute(age: String, importantPoint: String, guardianAvailable: String, active: String, nowadmission: String): String {
            return "result/$age/$importantPoint/$guardianAvailable/$active/$nowadmission"
        }
    }
}