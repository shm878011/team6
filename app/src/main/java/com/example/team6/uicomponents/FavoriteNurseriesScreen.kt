package com.example.team6.uicomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.team6.model.KinderInfo
import com.example.team6.model.Nursery
import com.example.team6.uicomponents.recommend.KindergartenCard
import com.example.team6.viewmodel.MainViewModel

@Composable
fun FavoriteNurseriesScreen(
    favorites: SnapshotStateList<KinderInfo>,
    navController: NavController,
    viewModel: MainViewModel
) {
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.clearClickList()
        isLoading = true
    }
    val clicklist by viewModel.clicklist.collectAsState()
    val clickData by viewModel.clickdata.collectAsState()

    SubPage(title = "찜한 어린이집", navController = navController) {
        if (favorites.isEmpty()) {
            Text("찜한 어린이집이 없습니다.", color = Color.Gray)
        } else {
            LazyColumn {
                items(favorites) { nursery ->
                    FavoriteNurseryItem(nursery, onClick = {
                        viewModel.setClickList(nursery)
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
                
                LaunchedEffect(clicklist!!.kindername) {
                    viewModel.loadReviews(clicklist!!.kindername)
                }
                
                val reviewCount by viewModel.reviewList.collectAsState()
                val averageRating by viewModel.averageRating.collectAsState()
                
                Box(modifier = Modifier.fillMaxSize()) {
                    NurseryDetailCard(
                        nursery = clickData,
                        isLiked = viewModel.isLiked(it),
                        reviewCount = reviewCount.size,
                        averageRating = averageRating,
                        onLikeToggle = { viewModel.toggleLike(it) },
                        onReviewClick = { viewModel.openReviewCard(clickData) },
                        onClose = { viewModel.clearClickList() },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )

                }
            }
        }
    }
}


@Composable
fun FavoriteNurseryItem(nursery: KinderInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(nursery.kindername, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            //Text("⭐ ${nursery.rating}")
            Text(nursery.addr, fontSize = 13.sp, color = Color.Gray)
        }
    }

}


