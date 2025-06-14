package com.example.team6.uicomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.team6.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items

@Composable
fun MyReviewsScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val myReviews by viewModel.myReviewList.collectAsState()

    // 화면에 진입하면 한번만 호출
    LaunchedEffect(Unit) {
        viewModel.loadMyReviews()
    }

    SubPage(title = "내 리뷰 보기", navController = navController) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (myReviews.isEmpty()) {
                Text("아직 내가 작성한 리뷰가 없습니다.", color = Color.Gray)
            } else {
                LazyColumn {
                    items(myReviews) { review ->
                        ReviewItem(review)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyReviewsPreview() {
    MyReviewsScreen(navController = rememberNavController())
}