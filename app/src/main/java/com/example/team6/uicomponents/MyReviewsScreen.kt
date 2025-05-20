package com.example.team6.uicomponents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MyReviewsScreen(navController: NavController) {
    SubPage(title = "내 리뷰 보기", navController = navController) {
        Text("내가 남긴 리뷰가 여기에 표시됩니다.")
    }
}

@Preview(showBackground = true)
@Composable
fun MyReviewsPreview() {
    MyReviewsScreen(navController = rememberNavController())
}