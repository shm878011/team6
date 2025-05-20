package com.example.team6.uicomponents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun FavoriteNurseriesScreen(navController: NavController) {
    SubPage(title = "관심 어린이집", navController = navController) {
        Text("좋아요 누른 어린이집 목록이 여기에 표시됩니다.")
    }
}


@Preview
@Composable
private fun FavoriteNurseriesScreenPrev() {
    FavoriteNurseriesScreen(navController = rememberNavController())
}