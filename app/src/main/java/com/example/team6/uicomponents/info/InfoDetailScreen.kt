package com.example.team6.uicomponents.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NapDetailScreen() {
    Column(Modifier.padding(24.dp)) {
        Text("낮잠 습관 들이기", fontSize = 22.sp)
        Spacer(Modifier.height(12.dp))
        Text("낮잠을 편하게 자도록 환경을 만들어주세요.\n1. 일정한 수면 시간\n2. 조용한 분위기\n3. 편안한 온도 등")
    }
}

@Composable
fun PlayDetailScreen() {
    Column(Modifier.padding(24.dp)) {
        Text("나이대별 놀이법 TOP5", fontSize = 22.sp)
        Spacer(Modifier.height(12.dp))
        Text("1세~5세 아이에게 적합한 놀이법을 소개합니다.\n블록 쌓기, 물놀이, 역할놀이 등")
    }
}

@Composable
fun EduDetailScreen() {
    Column(Modifier.padding(24.dp)) {
        Text("입소신청 완전정리", fontSize = 22.sp)
        Spacer(Modifier.height(12.dp))
        Text("어린이집 우선순위 기준:\n1. 주민등록 주소\n2. 부모 취업 여부\n3. 형제/자매 재원 여부")
    }
}

@Composable
fun FoodDetailScreen() {
    Column(Modifier.padding(24.dp)) {
        Text("식습관 기르기", fontSize = 22.sp)
        Spacer(Modifier.height(12.dp))
        Text("올바른 식습관을 위해:\n1. 편식하지 않도록 지도\n2. 다양한 식재료 경험\n3. 함께 요리해보기")
    }
}