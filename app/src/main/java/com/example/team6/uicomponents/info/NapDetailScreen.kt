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
fun NapDetailScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "낮잠 습관 들이기",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "낮잠은 영유아의 두뇌 발달과 정서 안정에 핵심적인 영향을 미칩니다. 생후 0~5세는 수면 습관을 형성하는 결정적 시기로, 낮잠의 질과 패턴은 전반적인 성장과 학습 능력에 직결됩니다.",
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Spacer(Modifier.height(24.dp))
        WebViewYoutubePlayer("https://www.youtube.com/watch?v=HqyV1uPWrfY")

        Spacer(Modifier.height(24.dp))

        SectionTitle("1. 규칙적인 낮잠 시간 설정")
        SectionText("매일 일정한 시간에 낮잠을 유도하면 생체 리듬이 안정되고, 아이에게 예측 가능한 패턴이 생겨 심리적 안정감이 형성됩니다.")

        SectionTitle("2. 수면 신호 만들기")
        SectionText(
            "낮잠 전에 반복되는 루틴을 정해주세요. 예: \n" +
                    "• 조용한 음악 재생\n" +
                    "• 커튼 닫기 및 조명 줄이기\n" +
                    "• 잠자리 동화 읽기\n" +
                    "→ 이런 습관은 '이제 잘 시간이야'라는 신호로 작용합니다."
        )

        SectionTitle("3. 적절한 수면 환경 만들기")
        SectionText(
            "• 조도: 커튼으로 빛 차단해 어두운 환경 조성\n" +
                    "• 온도: 20~22도 유지 (과도한 이불 금지)\n" +
                    "• 소음: 백색소음(white noise)은 배경음을 차단해 수면 유도에 효과적"
        )

        SectionTitle("4. 나이별 권장 낮잠 시간")
        SectionText(
            "• 6~12개월: 하루 2회, 각 1~1.5시간\n" +
                    "• 1~2세: 하루 1~2회, 총 1~3시간\n" +
                    "• 3~5세: 하루 1회, 약 1시간\n" +
                    "※ 5세 이후부터는 필요에 따라 유연하게 접근"
        )

        SectionTitle("5. 과도한 낮잠은 밤잠 방해")
        SectionText("낮잠이 지나치게 길면 야간 수면에 방해가 됩니다. 전체 수면 시간은 연령에 맞추되, 낮잠은 분산해서 조절하세요.")

        SectionTitle("6. 억지로 재우기보다는 기다리기")
        SectionText("졸려할 때 자연스럽게 잘 수 있도록 기다려주세요. 억지로 눕히는 것은 수면에 대한 부정적 인식을 줄 수 있습니다.")

        Spacer(Modifier.height(28.dp))

        Text(
            text = "출처:\n• 대한소아청소년과학회 수면 가이드라인",
            fontSize = 10.sp,
            color = Color.Gray,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
fun SectionText(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        lineHeight = 22.sp
    )
    Spacer(Modifier.height(20.dp))
}