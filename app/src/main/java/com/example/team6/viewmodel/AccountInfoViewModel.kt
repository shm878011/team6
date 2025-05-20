package com.example.team6.viewmodel

import androidx.lifecycle.ViewModel
import com.example.team6.model.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class AccountInfoViewModel : ViewModel() {

    private val _userInfo = MutableStateFlow(
        UserInfo(
            name = "홍길동",
            username = "konkuk",
            email = "konkuk@gmail.com",
            passwordMasked = "************"
        )
    )
    val userInfo: StateFlow<UserInfo> = _userInfo

    fun logout() {
        // TODO: 로그아웃 처리 로직 추가 (예: 상태 초기화, navigate 등)
    }
}
