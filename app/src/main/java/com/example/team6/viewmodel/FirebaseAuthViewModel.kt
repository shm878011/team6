package com.example.team6.viewmodel

import androidx.lifecycle.ViewModel
import com.example.team6.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirebaseAuthViewModel : ViewModel() {
    private val auth =  FirebaseAuth.getInstance()
    private val _nickname = MutableStateFlow<String>("")
    val nickname: StateFlow<String> = _nickname

    //닉네임을 DB에서 읽어오는 함수
    fun fetchNicknameFromDatabase() {
        val uid = auth.currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid/nickname")

        ref.get().addOnSuccessListener { snapshot ->
            _nickname.value = snapshot.getValue(String::class.java) ?: ""
        }.addOnFailureListener {
            _nickname.value = ""
        }
    }
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
    private val _authResult = MutableStateFlow<String?>(null)
    val authResult: StateFlow<String?> = _authResult

    fun signup(email: String, password: String, nickname: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveNicknameToDatabase(nickname)
                    _authResult.value = "회원가입 성공"
                } else {
                    _authResult.value = "회원가입 실패: ${task.exception?.message}"
                }
            }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authResult.value = if (task.isSuccessful) "로그인 성공" else "로그인 실패: ${task.exception?.message}"
            }
    }

    fun logout() {
        auth.signOut()
        _authResult.value = "로그아웃 완료"
    }

    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    fun saveNicknameToDatabase(nickname: String) {
        val uid = auth.currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        ref.child("nickname").setValue(nickname)
    }
}