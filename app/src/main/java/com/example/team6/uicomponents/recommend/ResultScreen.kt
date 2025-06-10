package com.example.team6.uicomponents.recommend

import android.R.attr.text
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ResultScreen(
    navController: NavController,
    age: String,
    importantPoint: String,
    guardianAvailable: String,
    active: String,
    now: String,
    viewModel: MainViewModel = viewModel()
) {

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
            text = "어린이집 추천 결과",
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
                KindergartenCard(item)
            }
        }
    }
}

@Composable
fun KindergartenCard(info: KinderInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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