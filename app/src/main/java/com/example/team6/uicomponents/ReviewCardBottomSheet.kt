package com.example.team6.uicomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.TextButton
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
                .padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)  //
        ) {
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 300.dp) // 전체 높이 제한 (필요시)
            ) {
                // 상단: 제목 & 닫기 버튼
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            onValueChange = { reviewText = it },
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

                // 🔽 리뷰 목록만 스크롤되도록
                if (reviews.isEmpty()) {
                    Text("아직 작성된 리뷰가 없습니다.", color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)  // 리뷰 목록만 확장되며 스크롤됨
                            .fillMaxWidth()
                    ) {
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
            Text(review.nickname)
            Spacer(modifier = Modifier.weight(1f))

            // 별점 표시: 채워진 별(노랑), 빈 별(회색)
            Row {
                repeat(review.rating) {
                    Text("★", color = Color(0xFFFFC107)) // 채워진 별: 노란색
                }
                repeat(5 - review.rating) {
                    Text("★", color = Color.LightGray) // 빈 별: 회색
                }
            }
        }
        Text(review.text)
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}




