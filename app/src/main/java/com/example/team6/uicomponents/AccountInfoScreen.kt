package com.example.team6.uicomponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.team6.model.UserInfo
import com.example.team6.viewmodel.AccountInfoViewModel
import com.example.team6.viewmodel.FirebaseAuthViewModel
import com.example.team6.viewmodel.MainViewModel

@Composable
fun AccountInfoScreen(
    navController: NavController,
    viewModel: FirebaseAuthViewModel = viewModel(),
    mainViewModel: MainViewModel,
    onLogout: () -> Unit
) {
    val userInfo = viewModel.getUserInfo()
    val isGuest = viewModel.isGuest.collectAsState().value

    // 🔹 DB에서 닉네임 가져오기
    LaunchedEffect(Unit) {
        viewModel.fetchNicknameFromDatabase()
    }

    SubPage(title = "계정 정보", navController = navController) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("회원 정보", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))

            if (isGuest == true || userInfo.name == "비회원") {
                Text("비회원입니다", style = MaterialTheme.typography.bodyLarge)
            } else {
                Text("이름: ${userInfo.name}", fontSize = 14.sp)

                Row(verticalAlignment = Alignment.CenterVertically) { // Text들을 세로 중앙 정렬
                    Text("이메일: ", fontSize = 14.sp) // "이메일: "까지 한 Text로 합칩니다.
                    Text(
                        userInfo.email,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text("비밀번호: ${userInfo.passwordMasked}", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextButton(onClick = {
                // 🔥 로그아웃 시 MainViewModel의 찜한 목록도 초기화
                mainViewModel.clearLikedNurseries()
                viewModel.logout()        // ViewModel 상태 초기화
                viewModel.checkLoginStatus()  // 상태 재확인 (선택 사항)
                onLogout()  // NavController.popBackStack() 등 상위 화면 이동 처리
            }) {
                Text("로그아웃", fontSize = 14.sp)
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//private fun AccountInfoPreview() {
//    val sampleUser = UserInfo(
//        name = "홍길동",
//        username = "konkuk",
//        email = "konkuk@gmail.com",
//        passwordMasked = "************"
//    )
//
//    AccountInfoScreen(
//        navController = rememberNavController(),
//        userInfo = sampleUser,
//        onLogout = { /* no-op */ }
//    )
//}

