package com.example.team6.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyPageScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("마이페이지 화면")
    }
}