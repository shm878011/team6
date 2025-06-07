package com.example.team6.uicomponents
import android.R.attr.text
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
        Text(text = "주소: ${kinderInfo.addr}")
        Text(text = "전화번호: ${kinderInfo.telno}")
        Text(text = "홈페이지 주소: ${kinderInfo.hpaddr ?: "정보 없음"}")
        Text(text = "운영시간: ${kinderInfo.opertime ?: "정보 없음"}")
        Text(text = "원장명: ${kinderInfo.ldgrname ?: "정보 없음"}")
        Text(text = "위도: ${kinderInfo.latitude }")
        Text(text = "경도: ${kinderInfo.longitude }")
        Text(text = "총원: ${kinderInfo.totalCapacity }")
        Text(text = "현원: ${kinderInfo.current }")
    }
}