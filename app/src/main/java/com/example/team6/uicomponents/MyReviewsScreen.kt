package com.example.team6.uicomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.team6.model.Review
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyReviewsScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val myReviews by viewModel.myReviewList.collectAsState()

    // 진입 시 내 리뷰 로딩
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
                        MyReviewItem(review = review) {
                            viewModel.deleteReview(review)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyReviewItem(review: Review, onDelete: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    val filledStars = review.rating
    val emptyStars = 5 - review.rating

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            Text(review.kinderName)
            Spacer(modifier = Modifier.weight(1f))

            Row {
                repeat(filledStars) {
                    Text("★", color = Color(0xFFFFC107))
                }
                repeat(emptyStars) {
                    Text("★", color = Color.LightGray)
                }
            }
        }
        Text(review.text)

        Text(
            text = "삭제",
            color = Color.Red,
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable {
                    showDialog = true
                }
        )

        // 삭제 확인 다이얼로그
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("리뷰 삭제") },
                text = { Text("정말 삭제하시겠습니까?") },
                confirmButton = {
                    TextButton(onClick = {
                        onDelete()
                        showDialog = false
                    }) {
                        Text("삭제", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyReviewsPreview() {
    MyReviewsScreen(navController = rememberNavController())
}