package com.example.team6.viewmodel

import androidx.lifecycle.ViewModel
import com.example.team6.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirebaseAuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    private val _authResult = MutableStateFlow<String?>(null)
    val authResult: StateFlow<String?> = _authResult

    private val _isGuest = MutableStateFlow<Boolean?>(null)
    val isGuest: StateFlow<Boolean?> = _isGuest

    // 앱 시작 시 로그인 여부 확인
    fun checkLoginStatus() {
        val user = auth.currentUser
        if (user != null) {
            _isGuest.value = false
            fetchNicknameFromDatabase()
        } else {
            _isGuest.value = true
            _nickname.value = "비회원"
        }
    }
    // 회원가입
    fun signup(email: String, password: String, nickname: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveNicknameToDatabase(nickname)
                    _authResult.value = "회원가입 성공"
                    _isGuest.value = false
                } else {
                    _authResult.value = "회원가입 실패: ${task.exception?.message}"
                }
            }
    }

    // 로그인
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authResult.value = "로그인 성공"
                    _isGuest.value = false
                    fetchNicknameFromDatabase()
                } else {
                    _authResult.value = "로그인 실패: ${task.exception?.message}"
                }
            }
    }

    //로그아웃
    fun logout() {
        auth.signOut()
        _authResult.value = "로그아웃 완료"
        _nickname.value = "비회원"
        _isGuest.value = true
    }

    //현재 사용자 정보 반환
    fun getUserInfo(): UserInfo {
        val email = auth.currentUser?.email ?: "이메일 없음"
        val nicknameValue = nickname.value
        val username = email.substringBefore("@")

        return UserInfo(
            name = nicknameValue,
            username = username,
            email = email,
            passwordMasked = "************"
        )
    }

    // 닉네임 저장
    fun saveNicknameToDatabase(nickname: String) {
        val uid = auth.currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        ref.child("nickname").setValue(nickname)
    }

    // 닉네임 불러오기
    fun fetchNicknameFromDatabase() {
        val uid = auth.currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid/nickname")

        ref.get().addOnSuccessListener { snapshot ->
            _nickname.value = snapshot.getValue(String::class.java) ?: ""
        }.addOnFailureListener {
            _nickname.value = ""
        }
    }
}