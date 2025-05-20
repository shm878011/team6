package com.example.team6.uicomponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun LocationSettingScreen(navController: NavController) {
    SubPage(title = "위치 설정", navController = navController) {
        // 내 위치
        Text("내 위치", fontWeight = FontWeight.SemiBold)
        Text("서울특별시 광진구 능동로 120 건국대학교")

        Spacer(modifier = Modifier.height(24.dp))

        // 위치 수정
        Text("위치 수정", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.weight(1f),
                placeholder = { Text("주소 검색") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { /* 주소 수정 로직 */ },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("수정")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 위치로 설정
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("현재 위치로 설정", modifier = Modifier.weight(1f))
            OutlinedButton(onClick = { /* GPS 위치 설정 로직 */ }) {
                Text("설정")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LocationSettingPreview() {
    LocationSettingScreen(navController = rememberNavController())
}