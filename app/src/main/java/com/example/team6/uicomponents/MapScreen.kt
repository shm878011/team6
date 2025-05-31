package com.example.team6.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.team6.R
import com.example.team6.model.Nursery
import com.example.team6.model.dummyNurseries
import com.example.team6.viewmodel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.naver.maps.map.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun NaverMapScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val defaultPosition = LatLng(37.5408, 127.0793)
    val currentPosition = viewModel.currentLocation ?: defaultPosition

    val kindergartenList by viewModel.kindergartenList.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(currentPosition, 15.0)
    }
    val locationSource = rememberFusedLocationSource()

    LaunchedEffect(viewModel.currentLocation) {
        viewModel.currentLocation?.let {
            cameraPositionState.move(
                CameraUpdate.toCameraPosition(CameraPosition(it, 15.0))
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        //지도
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            locationSource = locationSource,
            properties = MapProperties(
                locationTrackingMode = if (viewModel.currentLocation != null)
                    LocationTrackingMode.Follow
                else LocationTrackingMode.None
            ),
            uiSettings = MapUiSettings(
                isLocationButtonEnabled = viewModel.currentLocation != null // 위치 설정되면 네이버 버튼 보이게
            )
        ) {
            //현재 위치 설정 전 → 건국대 마커 표시
            if (viewModel.currentLocation == null) {
                Marker(
                    state = rememberMarkerState(position = defaultPosition),
                    captionText = "건국대학교"
                )
            }
        }

        // 커스텀 내 위치 버튼 (초기 상태에서만 보여짐)
        if (viewModel.currentLocation == null) {
            IconButton(
                onClick = {
                    cameraPositionState.move(
                        CameraUpdate.toCameraPosition(CameraPosition(defaultPosition, 15.0))
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(Color.White, shape = CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_my_location_24),
                    contentDescription = "내 위치로 이동",
                    tint = Color.Black
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun MapScreen(viewModel: MainViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    var showFilter by remember { mutableStateOf(false) }
    var selectedNursery by remember { mutableStateOf<Nursery?>(null) }
    var query by remember { mutableStateOf("") }
    var filteredNurseries by remember { mutableStateOf<List<Nursery>>(emptyList()) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val likedNurseries = viewModel.likedNurseries

    // 💡 항상 UI를 보여줌
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            NaverMapScreen(modifier = Modifier.fillMaxSize(),viewModel)
        }

        // 상단 검색/필터 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    filteredNurseries = dummyNurseries.filter { nursery ->
                        nursery.name.contains(query, ignoreCase = true)
                    }
                    showBottomSheet = filteredNurseries.isNotEmpty()
                },
                placeholder = { Text("검색") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                contentDescription = "필터",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { showFilter = true }
            )
        }

        // 💡 조건부로 BottomSheet 띄우기
        if (filteredNurseries.isNotEmpty() && showBottomSheet) {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent = {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(filteredNurseries) { nursery ->
                            NurseryListItem(nursery = nursery, onClick = {
                                selectedNursery = nursery
                                scope.launch {
                                    scaffoldState.bottomSheetState.partialExpand()
                                }
                            })
                        }
                    }
                },
                sheetPeekHeight = 64.dp,
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            ) {}
        }

        // 필터 모달
        if (showFilter) {
            FilterModal(
                onClose = { showFilter = false },
                onFilterApplied = { selectedDistance, selectedConditions ->
                    filteredNurseries = dummyNurseries.filter { nursery ->
                        selectedConditions.all { cond ->
                            when (cond) {
                                "입소 가능" -> nursery.current < nursery.capacity
                                "통학차량 여부" -> nursery.hasBus == "Y"
                                "놀이터 여부" -> nursery.playgroundCount > 0
                                "주변 어린이 보호구역" -> true
                                else -> true
                            }
                        }
                    }
                    showBottomSheet = filteredNurseries.isNotEmpty()
                    showFilter = false
                }
            )
        }

        // 상세 정보 카드
        selectedNursery?.let {
            NurseryDetailCard(
                nursery = it,
                isLiked = viewModel.isLiked(it),
                onLikeToggle = { viewModel.toggleLike(it) },
                onReviewClick = { /* TODO */ },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}


    @Composable
fun FilterModal(
    onClose: () -> Unit,
    onFilterApplied: (selectedDistance: String, selectedConditions: List<String>) -> Unit
) {
    val distances = listOf("500m", "1km", "3km", "5km", "10km")
    val conditions = listOf("입소 가능", "통학차량 여부", "놀이터 여부", "주변 어린이 보호구역")

    var selectedDistance by remember { mutableStateOf("1km") }
    val selectedConditions = remember { mutableStateListOf<String>() }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("필터", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("거리", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    distances.forEach {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(
                                selected = selectedDistance == it,
                                onClick = { selectedDistance = it }
                            )
                            Text(it, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("조건", fontWeight = FontWeight.Medium)
                conditions.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = selectedConditions.contains(it),
                            onCheckedChange = { checked ->
                                if (checked) selectedConditions.add(it)
                                else selectedConditions.remove(it)
                            }
                        )
                        Text(it)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onFilterApplied(selectedDistance, selectedConditions)
            }) {
                Text("적용")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("닫기")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
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
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    onReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(nursery.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "찜",
                    tint = if (isLiked) Color.Red else Color.Gray,
                    modifier = Modifier.clickable { onLikeToggle() }
                )
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
            Row(verticalAlignment = Alignment.CenterVertically) {
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