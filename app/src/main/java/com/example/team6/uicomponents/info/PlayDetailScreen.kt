package com.example.team6.uicomponents.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayDetailScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "나이대별 놀이법 TOP 5",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "아이의 발달 단계에 맞는 놀이는 인지, 신체, 사회성, 언어 능력을 고르게 발달시키는 데 핵심적인 역할을 합니다. 아래는 1세부터 5세까지 연령별 추천 놀이 TOP 5입니다.",
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Spacer(Modifier.height(24.dp))

        val ageGroups = listOf(
            "1세 (탐색기 시기)" to listOf(
                "촉감 놀이: 부드러운 천, 차가운 젤 등 다양한 감각 자극",
                "거울 놀이: 거울에 비친 자신을 보며 자아 인식",
                "간단한 소리나는 장난감: 원인과 결과를 배우기 시작",
                "부모 따라하기 놀이: 표정 따라하기, 박수 치기",
                "음악과 율동: 리듬에 맞춰 몸 움직이기"
            ),
            "2세 (자율성 형성 시기)" to listOf(
                "블록 쌓기: 소근육 발달과 공간 인식",
                "숨바꼭질: 사물의 지속성 개념 인지",
                "단순 퍼즐 맞추기: 문제 해결 능력",
                "그림 그리기: 창의성과 표현력 향상",
                "소꿉놀이: 상상력과 역할 놀이 시작"
            ),
            "3세 (사회적 기술 시작 시기)" to listOf(
                "역할놀이: 엄마·아빠 놀이, 병원 놀이 등 상호작용 증진",
                "간단한 게임: 순서 기다리기, 규칙 배우기",
                "색칠하기: 집중력과 색 인지 향상",
                "점토 놀이: 촉감 자극과 창의적 표현",
                "간단한 체조: 신체 조절 능력 향상"
            ),
            "4세 (인지·언어 폭발 시기)" to listOf(
                "이야기 짓기 놀이: 언어 표현과 논리력 향상",
                "보드게임: 규칙 이해와 사회성 훈련",
                "블록 설계놀이: 구조 개념 발달",
                "신체 활동: 줄넘기, 공놀이 등 대근육 조절",
                "역할극: 감정 표현과 공감 능력 향상"
            ),
            "5세 (문해력·자기조절 형성 시기)" to listOf(
                "글자·숫자 놀이: 문해력의 기초 형성",
                "팀워크 놀이: 협동심과 리더십",
                "간단한 과학 실험 놀이: 호기심과 탐구력 증진",
                "종이접기: 순서 기억과 도식 사고력",
                "이야기 이어가기: 창의력과 사고 연결 능력"
            )
        )

        ageGroups.forEach { (ageTitle, playList) ->
            Spacer(Modifier.height(20.dp))
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0x55F9F6E6)),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "◼︎ $ageTitle", fontSize = 17.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                    Spacer(Modifier.height(10.dp))
                    playList.forEach { item ->
                        Text(text = "• $item", fontSize = 15.sp, lineHeight = 22.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "출처:\n• CDC Early Childhood Development\n• 육아정책연구소 유아놀이행동연구 (2022)",
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp,
            modifier = Modifier.align(Alignment.Start)
        )
    }
}