package com.example.team6.model

data class Review(
    val userId: String = "",
    val nickname: String = "",
    val rating: Int = 5,
    val text: String = "",
    val timestamp: Long = 0,
    var key: String? = null  // 삭제용 키
)
