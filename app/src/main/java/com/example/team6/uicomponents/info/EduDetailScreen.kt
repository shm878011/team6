package com.example.team6.uicomponents.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EduDetailScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "올바른 훈육 방법 💛",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "훈육은 처벌이 아닌, 아이가 바람직한 행동을 배우고 스스로 감정을 조절하는 능력을 기르는 교육적 과정입니다. 다음은 효과적인 훈육의 핵심 원칙들입니다.",
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Spacer(Modifier.height(24.dp))

        Text("1. 일관성 있는 규칙 설정", fontSize = 17.sp, fontWeight = FontWeight.Bold,color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "규칙은 짧고 명확하게 전달하고, 반복적으로 설명해 주세요. 매번 같은 기준을 유지하면 아이가 혼란을 느끼지 않고 안정감을 느낍니다.🫶",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("2. 긍정적 강화 (Positive Reinforcement)", fontSize = 17.sp, fontWeight = FontWeight.Bold,color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "아이의 바람직한 행동을 발견했을 때 즉시 칭찬하거나 작은 보상을 주세요! '스스로 정리해서 정말 기뻐!'와 같이 구체적인 칭찬이 더 효과적입니다.🥰",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("3. 자연적 결과 허용", fontSize = 17.sp, fontWeight = FontWeight.Bold,color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "아이의 행동 결과를 스스로 경험하게 해주세요. 예를 들어, 장난감을 던졌다면 일정 시간 그 장난감을 치우는 방식으로 자연스럽게 결과를 느끼게 합니다.😉",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("4. 타임아웃", fontSize = 17.sp, fontWeight = FontWeight.Bold,color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "과도한 행동이나 공격적 반응이 나타날 경우, 자극이 없는 공간에서 아이를 1~5분 정도 혼자 있게 하여 감정을 가라앉힐 시간을 주세요. 나이 × 1분이 적절합니다.🙌",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("5. 감정 명명 및 공감", fontSize = 17.sp, fontWeight = FontWeight.Bold,color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "아이의 감정을 말로 표현해 주며 공감해 주세요.\n '지금 속상했구나. 엄마도 그런 기분 느껴..🥲' \n 공감 받은 아이는 반항보다는 소통을 시도하게 됩니다.",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("6. 신체적 처벌은 피하기", fontSize = 17.sp, fontWeight = FontWeight.Bold,color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "체벌은 아이에게 두려움과 낮은 자존감을 유발할 수 있으며, 장기적으로 역효과가 큽니다! 미국 소아과학회(AAP)에서는 체벌을 금지하고 대화 중심 훈육을 권장합니다.😊",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("7. 부모 자신의 감정 조절", fontSize = 17.sp, fontWeight = FontWeight.Bold,color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "부모가 화가 났을 때는 잠시 멈추고 감정을 조절한 뒤 대화에 나서야 합니다. 부모의 감정 표현은 아이에게 자기 조절의 모델이 됩니다.🙂",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(28.dp))

        Text(
            text = "출처:\n• 육아정책연구소 부모교육 자료집\n• Gershoff, E.T. (2013), Child Development Perspectives",
            fontSize = 8.sp,
            color = Color.Gray,
            lineHeight = 10.sp
        )
    }
}