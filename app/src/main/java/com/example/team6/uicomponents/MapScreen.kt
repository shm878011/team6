package com.example.team6.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team6.R
import com.example.team6.model.Nursery
import com.example.team6.model.dummyNurseries
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showFilter by remember { mutableStateOf(false) }
    var selectedNursery by remember { mutableStateOf<Nursery?>(null) }
    var filteredNurseries by remember { mutableStateOf(dummyNurseries) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 지도 배경 (Placeholder)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray))

        // 상단 바 (메뉴, 검색창, 필터)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "메뉴")
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("검색") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                contentDescription = "필터 아이콘",
                modifier = Modifier
                    .size(24.dp)
                    .padding(16.dp)
                    .clickable { showFilter = true }
            )
        }

        // 필터 UI
        if (showFilter) {
            FilterModal(onClose = { showFilter = false }) {
                // 필터 적용 시 nursery 갱신 (예시)
                filteredNurseries = dummyNurseries.take(2)
                showFilter = false
            }
        }

        // 바텀 시트 (필터링 결과 리스트)
        if (filteredNurseries.isNotEmpty()) {
            ModalBottomSheet(
                onDismissRequest = {},
                sheetState = sheetState,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(filteredNurseries) { nursery ->
                        NurseryListItem(nursery = nursery, onClick = {
                            selectedNursery = nursery
                            scope.launch { sheetState.hide() }
                        })
                    }
                }
            }
        }

        // 상세 정보 카드
        selectedNursery?.let {
            NurseryDetailCard(
                nursery = it,
                onReviewClick = { /* 리뷰 보기 기능 연결 예정*/ },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun FilterModal(onClose: () -> Unit, onFilterApplied: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("필터", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            // 거리 필터 (간단화)
            Text("거리")
            Row {
                listOf("500m", "1km", "3km").forEach {
                    Text(it, modifier = Modifier.padding(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // 조건 체크박스들 (입소, 통학, 놀이터, 보호구역)
            Text("조건")
            listOf("입소 가능", "통학차량 여부", "놀이터 여부", "주변 어린이 보호구역").forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text(it)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onFilterApplied, modifier = Modifier.align(Alignment.End)) {
                Text("완료")
            }
        }
    }
}

@Composable
fun NurseryListItem(nursery: Nursery, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(nursery.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("⭐ ${nursery.rating}")
            Text(nursery.address, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun NurseryDetailCard(
    nursery: Nursery,
    onReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(nursery.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.FavoriteBorder, contentDescription = "찜")
            }
            Text("⭐ ${nursery.rating}   ", fontSize = 14.sp)
            Text(nursery.address)
            Text(nursery.phone)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text("CCTV: ${nursery.cctvCount}", modifier = Modifier.weight(1f))
                Text("놀이터: ${nursery.playgroundCount}", modifier = Modifier.weight(1f))
                Text("보육실: ${nursery.roomCount}", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "정원/현원: ${nursery.capacity}/${nursery.current}",
                    modifier = Modifier.weight(1f)
                )
                Text("교직원 수: ${nursery.staffCount}", modifier = Modifier.weight(1f))
                Text("통학차량: ${nursery.hasBus}", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "리뷰 ${nursery.reviewCount}",
                color = Color.Blue,
                modifier = Modifier.clickable { onReviewClick() }
            )
        }
    }
}


@Preview
@Composable
private fun MapScreenprev() {
    MapScreen()
}