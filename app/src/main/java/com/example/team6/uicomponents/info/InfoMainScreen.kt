package com.example.team6.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.team6.R

data class InfoItem(val title: String, val description: String, val iconRes: Int)

val infoList = listOf(
    InfoItem("낮잠 습관 들이기", "아이가 편안하게 낮잠 자는 법", R.drawable.img_info_nap),
    InfoItem("나이대별 놀이법 TOP5", "집에서도 쉽게 할 수 있는 창의 놀이", R.drawable.img_info_play),
    InfoItem("입소신청 완전정리", "어린이집 신청 우선순위 기준 총정리", R.drawable.img_info_kin),
    InfoItem("식습관 기르기", "아이에게 올바른 식습관을 길러주는 팁", R.drawable.img_info_food),
    InfoItem("올바른 훈육 방법", "아이의 마음을 길러주는 긍정육아", R.drawable.img_info_edu)
)

@Composable
fun InfoScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(start = 16.dp)
    ) {
        Text(
            text = "우리 아이 이야기",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "아이의 성장과 부모님을 위한 팁을 모았어요.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn {
            items(infoList) { item ->
                InfoListItem(item = item, navController = navController)
            }
        }
    }
}

@Composable
fun InfoListItem(item: InfoItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("info_detail/${item.title}")
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.title,
            modifier = Modifier
                .size(72.dp)
                .padding(end = 16.dp)
        )
        Column {
            Text(text = item.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = item.description, fontSize = 13.sp, color = Color.Gray)
        }
    }
}