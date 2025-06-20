package com.example.team6.uicomponents.recommend

import android.R.attr.onClick
import android.R.attr.text
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.team6.model.KinderInfo
import com.example.team6.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.team6.uicomponents.NurseryDetailCard
import com.example.team6.uicomponents.NurseryListItem
import com.example.team6.uicomponents.ReviewCardBottomSheet


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ResultScreen(
    navController: NavController,
    age: String,
    importantPoint: String,
    guardianAvailable: String,
    active: String,
    now: String,
    viewModel: MainViewModel
) {
    var isLoading by remember { mutableStateOf(false) }
    val selectedReviewKinder = viewModel.selectedReviewNursery.collectAsState().value


    LaunchedEffect(Unit) {
        viewModel.clearClickList()
        viewModel.closeReviewCard()
        isLoading = true
    }
    val clicklist by viewModel.clicklist.collectAsState()
    val clickData by viewModel.clickdata.collectAsState()

    val currentAddress by viewModel.addressText.collectAsState()
    val scope = rememberCoroutineScope()
    val checklist by viewModel.checklist.collectAsState()

    Log.d("MainViewModelAPI", "${age} ${importantPoint} ${guardianAvailable} ${active} ${now}")

    scope.launch {
        val sidoSggCodeMap = viewModel.nameToMapCode
        var sido = ""
        var sgg = ""
        for ((sidoCandidate, sggCandidate) in sidoSggCodeMap.keys) {
            if (currentAddress.contains(sidoCandidate) && currentAddress.contains(sggCandidate)) {
                sido = sidoCandidate
                sgg = sggCandidate
                break
            }
        }
        Log.d("MainViewModelAPI","Asdf")
        val fetchJobs = mutableListOf<Job>()

        fetchJobs.add(launch { viewModel.fetchKindergartenData(sido, sgg) })

        if(guardianAvailable == "네")
        {
            fetchJobs.add(launch { viewModel.fetchKindergartensWithSchoolBus(sido, sgg) })
        }
        else{
            viewModel.RemoveBus()
        }
        if(active == "네")
        {
            fetchJobs.add(launch { viewModel.fetchKindergartensWithSafePlayground(sido, sgg) })
        }
        else{
            viewModel.RemovePlayground()
        }
        if(importantPoint == "네")
        {
            fetchJobs.add(launch { viewModel.fetchKindergartensWithSafeCCTV(sido, sgg) })
        }
        else{
            viewModel.RemoveCCTV()
        }

        if(now == "네")
        {
            viewModel.Canadmission(true)
        }
        else{
            viewModel.Canadmission(false)
        }

        fetchJobs.joinAll()

        viewModel.updateChecklist()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "유치원 추천 결과",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            items(checklist) { item ->
                KindergartenCard(item, onClick = {
                    viewModel.setClickList(item)
                })
            }
        }
    }
    if (isLoading) {
        clicklist?.let {
            val sidoSggCodeMap = viewModel.nameToMapCode
            var sido = ""
            var sgg = ""
            for ((sidoCandidate, sggCandidate) in sidoSggCodeMap.keys) {
                if (clicklist!!.addr.contains(sidoCandidate) && clicklist!!.addr.contains(
                        sggCandidate
                    )
                ) {
                    sido = sidoCandidate
                    sgg = sggCandidate
                    break
                }
            }
            LaunchedEffect(sido, sgg, clicklist!!.kindername) {
                viewModel.populateClickData(sido, sgg, clicklist!!.kindername)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                NurseryDetailCard(
                    nursery = clickData,
                    isLiked = viewModel.isLiked(it),
                    reviewCount = viewModel.reviewList.collectAsState().value.size,
                    averageRating = viewModel.averageRating.collectAsState().value,
                    onLikeToggle = { viewModel.toggleLike(it) },
                    onReviewClick = { viewModel.openReviewCard(clickData) },
                    onClose = { viewModel.clearClickList() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )

            }
        }
    }
    if (selectedReviewKinder != null) {
        ReviewCardBottomSheet(viewModel = viewModel)
    }
}

@Composable
fun KindergartenCard(info: KinderInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F8)
        )

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = info.kindername, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "전화번호: ${info.telno}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "주소: ${info.addr}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "운영 시간: ${info.opertime}", fontSize = 14.sp)
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = Icons.Default.Star,
//                    contentDescription = "Rating",
//                    tint = Color(0xFFFFD700),
//                    modifier = Modifier.size(18.dp)
//                )
//                Text(text = "${info.rating}", fontSize = 14.sp)
//                Text(text = "   거리 ${info.distance}", fontSize = 14.sp)
//            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = info.addr, fontSize = 13.sp)
        }
    }
}
