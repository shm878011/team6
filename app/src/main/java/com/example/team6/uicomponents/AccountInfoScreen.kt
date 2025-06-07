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

@Composable
fun AccountInfoScreen(
    navController: NavController,
    viewModel: FirebaseAuthViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    val nickname by viewModel.nickname.collectAsState()

    // 🔹 DB에서 닉네임 가져오기
    LaunchedEffect(Unit) {
        viewModel.fetchNicknameFromDatabase()
    }
    val userInfo = viewModel.getUserInfo()

    SubPage(title = "계정 정보", navController = navController) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("회원 정보", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Text("이름: ${userInfo.name}", fontSize = 14.sp)
            Text("아이디: ${userInfo.username}", fontSize = 14.sp)

            Text("이메일:", fontSize = 14.sp)
            Text(
                userInfo.email,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )

            Text("비밀번호: ${userInfo.passwordMasked}", fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextButton(onClick = onLogout) {
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

