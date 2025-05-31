package com.example.team6.uicomponents
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.team6.model.KinderInfo // API 데이터 모델 임포트
import com.example.team6.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = viewModel() // ViewModel 인스턴스를 얻음
) {
    // ViewModel의 kindergartenList StateFlow를 관찰합니다.
    // 데이터가 변경될 때마다 UI가 자동으로 리컴포즈됩니다.
    val kindergartenList by mainViewModel.kindergartenList.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "총 유치원 수: ${kindergartenList.size}개", modifier = Modifier.padding(bottom = 8.dp))

        if (kindergartenList.isEmpty()) {
            Text("유치원 정보를 불러오는 중입니다. 잠시 기다려주세요...")
        } else {
            LazyColumn {
                items(kindergartenList) { kinderInfo ->
                    KindergartenItem(kinderInfo = kinderInfo)
                }
            }
        }
    }
}

@Composable
fun KindergartenItem(kinderInfo: KinderInfo) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "유치원명: ${kinderInfo.kindername}")
        Text(text = "교육청: ${kinderInfo.officeedu}")
        Text(text = "교육지원청: ${kinderInfo.subofficeedu}")
        Text(text = "설립유형: ${kinderInfo.establish ?: "정보 없음"}")
        Text(text = "시도코드: ${kinderInfo.sidoCode ?: "정보 없음"}")
        Text(text = "시도코드: ${kinderInfo.sggCode ?: "정보 없음"}")
        // 필요에 따라 더 많은 정보를 여기에 표시합니다.
    }
}