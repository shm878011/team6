package com.example.team6.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InfoScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("정보 화면")
    }
}

@Preview
@Composable
private fun InfoScreenPrev() {
    InfoScreen()

}
