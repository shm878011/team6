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

    // 🔥 회원가입 입력값 검증 함수
    fun validateSignupInput(email: String, password: String, confirmPassword: String, nickname: String): String? {
        return when {
            email.trim().isEmpty() -> "이메일을 입력해주세요"
            password.trim().isEmpty() -> "비밀번호를 입력해주세요"
            confirmPassword.trim().isEmpty() -> "비밀번호 확인을 입력해주세요"
            nickname.trim().isEmpty() -> "닉네임을 입력해주세요"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "올바른 이메일 형식을 입력해주세요"
            password.length < 6 -> "비밀번호는 6자 이상이어야 합니다"
            password != confirmPassword -> "비밀번호가 일치하지 않습니다"
            else -> null
        }
    }

    // 회원가입
    fun signup(email: String, password: String, nickname: String) {
        // 🔥 입력값 검증
        val validationError = validateSignupInput(email, password, password, nickname)
        if (validationError != null) {
            _authResult.value = validationError
            return
        }
        
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    // ✅ displayName 설정 추가
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(nickname.trim())
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                saveNicknameToDatabase(nickname.trim())
                                _authResult.value = "회원가입 성공"
                                _isGuest.value = false
                            } else {
                                _authResult.value = "프로필 설정 실패: ${updateTask.exception?.message}"
                            }
                        }
                } else {
                    _authResult.value = "회원가입 실패: ${task.exception?.message}"
                }
            }
    }

    // 🔥 입력값 검증 함수
    fun validateLoginInput(email: String, password: String): String? {
        return when {
            email.trim().isEmpty() -> "이메일을 입력해주세요"
            password.trim().isEmpty() -> "비밀번호를 입력해주세요"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "올바른 이메일 형식을 입력해주세요"
            password.length < 6 -> "비밀번호는 6자 이상이어야 합니다"
            else -> null
        }
    }

    // 로그인
    fun login(email: String, password: String) {
        // 🔥 입력값 검증
        val validationError = validateLoginInput(email, password)
        if (validationError != null) {
            _authResult.value = validationError
            return
        }
        
        auth.signInWithEmailAndPassword(email.trim(), password)
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
        _authResult.value = ""
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