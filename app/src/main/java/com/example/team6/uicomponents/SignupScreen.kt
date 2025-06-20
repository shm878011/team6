package com.example.team6.uicomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.team6.viewmodel.FirebaseAuthViewModel


@Composable
fun SignupScreen(
    onBackToLogin: () -> Unit,
    viewModel: FirebaseAuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    val result by viewModel.authResult.collectAsState()
    var localMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("애지중지", fontSize = 28.sp, color = Color(0xFFFFC107))

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("이메일") }) // 이메일
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("비밀번호") })
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("비밀번호 확인") })
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = nickname, onValueChange = { nickname = it }, label = { Text("닉네임") })

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (password != confirmPassword) {
                    localMessage = "비밀번호가 일치하지 않습니다."
                } else if (email.isBlank() || password.isBlank()) {
                    localMessage = "아이디와 비밀번호를 입력해주세요."
                } else {
                    viewModel.signup(email, password, nickname)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Text("회원가입", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))

        (result ?: localMessage)?.let {
            Text(
                text = it,
                color = if (it.contains("성공")) Color.Blue else Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("이미 계정이 있으신가요?", fontSize = 14.sp)
        TextButton(onClick = onBackToLogin) {
            Text("로그인")
        }
    }
}

@Preview(showBackground = true, name = "Signup Preview")
@Composable
fun SignupScreenPreview() {
    SignupScreen(
        onBackToLogin = {}
    )
}
