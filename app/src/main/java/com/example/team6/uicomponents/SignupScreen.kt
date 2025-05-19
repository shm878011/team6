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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SignupScreen(onBackToLogin : () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("애지중지", fontSize = 28.sp, color = Color(0xFFFFC107))

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("아이디") })
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("비밀번호") })
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("비밀번호 확인") })
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("닉네임") })

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {/* 회원가입 로직 */},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Text("회원가입", color = Color.Black)
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
