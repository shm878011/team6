package com.example.team6.uicomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team6.viewmodel.MainViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.team6.model.Review


@Composable
fun ReviewCardBottomSheet(viewModel: MainViewModel) {
    val nursery = viewModel.selectedReviewNursery.collectAsState().value
    val reviews = viewModel.reviewList.collectAsState().value

    var isWriting by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }

    if (nursery != null) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // 상단 제목 + 닫기
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.closeReviewCard() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                    Text("리뷰 목록", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.closeReviewCard() }) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                }

                // 리뷰 작성 버튼
                Button(
                    onClick = { isWriting = !isWriting },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isWriting) "작성 취소" else "리뷰 작성하기")
                }

                // 리뷰 작성 폼
                if (isWriting) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = { text ->
                                reviewText = text
                            },
                            label = { Text("리뷰 내용") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("평점: ${rating.toInt()} ★")
                        Slider(
                            value = rating,
                            onValueChange = { rating = it },
                            valueRange = 1f..5f,
                            steps = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.submitReview(nursery.name, reviewText, rating.toInt())
                                reviewText = ""
                                rating = 0f
                                isWriting = false
                            },
                            enabled = reviewText.isNotBlank() && rating >= 1f
                        ) {
                            Text("작성 완료")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 리뷰 리스트
                if (reviews.isEmpty()) {
                    Text("아직 작성된 리뷰가 없습니다.", color = Color.Gray)
                } else {
                    LazyColumn {
                        items(reviews) { review ->
                            ReviewItem(review)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            Text(review.nickname, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("★ ${review.rating}", color = Color(0xFFFFC107))
            Spacer(modifier = Modifier.weight(1f))
        }
        Text(review.text)

    }
}
