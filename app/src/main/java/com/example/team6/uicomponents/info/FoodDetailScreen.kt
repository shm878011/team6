package com.example.team6.uicomponents.info

import android.R.attr.fontWeight
import android.R.attr.lineHeight
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
fun FoodDetailScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "식습관 기르기 🍽",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "영유아기의 식습관은 평생 건강에 결정적인 영향을 미칩니다. 이 시기의 식경험은 향후 식품 선호도와 영양 상태를 좌우하며, 다양한 식습관 교육은 조기에 시작될수록 효과적입니다.",
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Spacer(Modifier.height(24.dp))

        Text("1. 정해진 시간에 식사하기 ️", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "규칙적인 식사 시간은 아이의 생체 리듬을 안정시키고, 과식이나 편식을 방지하는 데 도움이 됩니다. 간식은 하루 1~2회 정도, 일정한 시간에 주는 것이 바람직합니다. 🍩",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("2. 다양한 식품군 경험하기 ", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "채소, 과일, 단백질, 유제품, 통곡물 등 다양한 식재료를 경험하게 해주세요! 🥦 아이가 싫어하는 음식이라도 최소 10~15회 이상 여러 번 노출해 보는 것이 좋습니다. 😊",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("3. 편식 방지법", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "음식에 대해 긍정적인 이미지를 형성하도록 도와주세요. 억지로 먹이기보다는 즐겁고 자연스럽게 접하게 하며, 가족이 함께 식사하며 모범을 보이는 것이 효과적입니다. 🙂",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("4. 함께 요리하기", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "간단한 요리 활동에 아이를 참여시키면 식사에 대한 흥미가 높아집니다. 예를 들어 재료 다듬기나 섞기 등을 함께해보세요! 자신이 만든 음식은 더 잘 먹는 경향이 있습니다.",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("5. 식사 중 전자기기 사용 금지", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "TV나 스마트폰을 보면서 식사하면 집중력이 떨어지고 포만감을 인지하는 능력이 저하되어 과식할 수 있습니다.😢 대화하며 식사하는 환경이 바람직합니다.",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(20.dp))

        Text("6. 칭찬은 구체적으로", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2))
        Spacer(Modifier.height(8.dp))
        Text(
            text = "행동 중심의 칭찬이 효과적입니다. '다 먹었네!'보다는 '브로콜리를 먼저 먹었구나, 잘했어! 브로콜리는 몸에 좋아!'와 같이 구체적인 피드백을 주세요. 🥰",
            fontSize = 15.sp,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(28.dp))

        Text(
            text = "✅ 식습관은 하루아침에 형성되지 않으며, 부모의 지속적이고 일관된 태도가 핵심입니다. 긍정적인 식사 환경이 아이의 평생 건강을 결정합니다.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(28.dp))

        Text(
            text = "출처:\n• 대한소아청소년영양학회\n• WHO 유아식 가이드라인",
            fontSize = 8.sp,
            color = Color.Gray,
            lineHeight = 10.sp
        )
    }
}