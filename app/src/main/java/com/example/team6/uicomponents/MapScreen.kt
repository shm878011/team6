package com.example.team6.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team6.R
import com.example.team6.model.Click
import com.example.team6.model.KinderInfo
import com.example.team6.model.Nursery
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
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
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

    val trackingMode = remember { mutableStateOf(LocationTrackingMode.None) }

    val checklist by viewModel.checklist.collectAsState()


    LaunchedEffect(viewModel.currentLocation) {
        viewModel.currentLocation?.let {
            cameraPositionState.move(
                CameraUpdate.toCameraPosition(CameraPosition(it, 15.0))
            )
            trackingMode.value = LocationTrackingMode.None // 수동 위치 설정 시, 자동 추적 해제
        }
    }



    Box(modifier = modifier.fillMaxSize()) {
        //지도
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            locationSource = locationSource,
            properties = MapProperties(
                locationTrackingMode = trackingMode.value // 상태 기반으로 추적 모드 설정
            ),
            uiSettings = MapUiSettings(
                isLocationButtonEnabled = false
            )
        ) {
            //현재 위치 설정 전 → 건국대 마커 표시
            if (viewModel.currentLocation == null) {
                Marker(
                    state = rememberMarkerState(position = defaultPosition),
                    captionText = "건국대학교"
                )
            }

            // 필터링된 유치원들 마커로 표시
            checklist.forEach { kindergarten ->
                if (kindergarten.latitude != 0.0 && kindergarten.longitude != 0.0) {
                    Marker(
                        state = rememberMarkerState(position = LatLng(kindergarten.latitude!!, kindergarten.longitude!!)),
                        captionText = kindergarten.kindername,
                        icon = OverlayImage.fromResource(R.drawable.marker),
                        width = 48.dp,
                        height = 48.dp,
                        onClick = {
                            // 주소 기반으로 시도/시군구 이름 추출
                            val sidoSggCodeMap = viewModel.nameToMapCode
                            var sido = ""
                            var sgg = ""
                            for ((sidoCandidate, sggCandidate) in sidoSggCodeMap.keys) {
                                if (kindergarten.addr.contains(sidoCandidate) && kindergarten.addr.contains(sggCandidate)) {
                                    sido = sidoCandidate
                                    sgg = sggCandidate
                                    break
                                }
                            }

                            viewModel.populateClickData(sido, sgg, kindergarten.kindername)
                            viewModel.setClickList(kindergarten)
                            true // 클릭 이벤트 소비
                        }
                    )
                }
            }
        }


        // 커스텀 내 위치 버튼
            IconButton(
                onClick = {
                    val targetPosition = viewModel.currentLocation ?: defaultPosition
                    cameraPositionState.move(CameraUpdate.toCameraPosition(CameraPosition(targetPosition, 15.0)))
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

    val kindergartenList by viewModel.kindergartenList.collectAsState()
    val checklist by viewModel.checklist.collectAsState()
    //var clicklist by remember { mutableStateOf<KinderInfo?>(null) }
    val clicklist by viewModel.clicklist.collectAsState()
    val clickData by viewModel.clickdata.collectAsState()


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
                    viewModel.Findlist(query)
                    showBottomSheet = checklist.isNotEmpty()
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
        if (checklist.isNotEmpty() && showBottomSheet) {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent = {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(checklist) { kinderinfo ->
                            NurseryListItem(kinderinfo = kinderinfo, onClick = {
                                //clicklist = kinderinfo
                                viewModel.setClickList(kinderinfo)
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
                    scope.launch { // 비동기 작업을 위한 코루틴 스코프 시작
                        val sido = "서울특별시"
                        val sgg = "광진구"

                        val fetchJobs = mutableListOf<Job>()

                        fetchJobs.add(launch { viewModel.fetchKindergartenData(sido, sgg) })

                        if("통학차량 여부" in selectedConditions)
                        {
                            fetchJobs.add(launch { viewModel.fetchKindergartensWithSchoolBus(sido, sgg) })
                        }
                        else{
                            viewModel.RemoveBus()
                        }
                        if("놀이터 여부" in selectedConditions)
                        {
                            fetchJobs.add(launch { viewModel.fetchKindergartensWithSafePlayground(sido, sgg) })
                        }
                        else{
                            viewModel.RemovePlayground()
                        }
                        if("CCTV 여부" in selectedConditions)
                        {
                            fetchJobs.add(launch { viewModel.fetchKindergartensWithSafeCCTV(sido, sgg) })
                        }
                        else{
                            viewModel.RemoveCCTV()
                        }

                        if("입소 가능" in selectedConditions)
                        {
                            viewModel.Canadmission(true)
                        }
                        else{
                            viewModel.Canadmission(false)
                        }

                        fetchJobs.joinAll()

                        viewModel.updateChecklist()
                        showBottomSheet = checklist.isNotEmpty()
                        showFilter = false
                    }
                }
            )
        }

        // 상세 정보 카드
        clicklist?.let {
            val sidoSggCodeMap = viewModel.nameToMapCode
            var sido = ""
            var sgg = ""
            for ((sidoCandidate, sggCandidate) in sidoSggCodeMap.keys) {
                if (clicklist!!.addr.contains(sidoCandidate) && clicklist!!.addr.contains(sggCandidate)) {
                    sido = sidoCandidate
                    sgg = sggCandidate
                    break
                }
            }
            LaunchedEffect(sido, sgg, clicklist!!.kindername) {
                viewModel.populateClickData(sido, sgg, clicklist!!.kindername)
            }
            NurseryDetailCard(
                nursery = clickData,
                isLiked = viewModel.isLiked(it),
                onLikeToggle = { viewModel.toggleLike(it) },
                onReviewClick = { /* TODO */ },
                onClose = { viewModel.clearClickList() },
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
    val conditions = listOf("입소 가능", "통학차량 여부", "놀이터 여부", "CCTV 여부")

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
fun NurseryListItem(kinderinfo: KinderInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(kinderinfo.kindername, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            //Text("⭐ ${nursery.rating}")
            Text(kinderinfo.addr, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun NurseryDetailCard(
    nursery: Click,
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    onReviewClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClose: () -> Unit

) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(nursery.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onClose) { //닫기버튼
                    Icon(Icons.Default.Close, contentDescription = "닫기")
                }
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "찜",
                    tint = if (isLiked) Color.Red else Color.Gray,
                    modifier = Modifier.clickable { onLikeToggle() }
                )
            }
            //Text("⭐ ${nursery.rating}   ", fontSize = 14.sp)
            Text(nursery.address)
            Text(nursery.phone.toString())
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text("CCTV: ${nursery.cctv_ist_total}", modifier = Modifier.weight(1f))
                Text("놀이터: ${nursery.plyg_ck_yn}", modifier = Modifier.weight(1f))
                Text("보육실: ${nursery.roomCount}", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "정원/현원: ${nursery.totalCapacity}/${nursery.current}",
                    modifier = Modifier.weight(1f)
                )
                Text("교직원 수: ${nursery.staffCount}", modifier = Modifier.weight(1f))
                Text("통학차량: ${nursery.vhcl_oprn_yn}", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "리뷰 ${nursery.reviewCount}",
//                color = Color.Blue,
//                modifier = Modifier.clickable { onReviewClick() }
//            )
        }
    }
}