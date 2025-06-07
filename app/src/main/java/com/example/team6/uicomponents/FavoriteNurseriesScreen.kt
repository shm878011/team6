package com.example.team6.uicomponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.team6.model.KinderInfo
import com.example.team6.model.Nursery

@Composable
fun FavoriteNurseriesScreen(
    favorites: SnapshotStateList<KinderInfo>,
    navController: NavController
) {
    SubPage(title = "찜한 어린이집", navController = navController) {
        if (favorites.isEmpty()) {
            Text("찜한 어린이집이 없습니다.", color = Color.Gray)
        } else {
            LazyColumn {
                items(favorites) { nursery ->
                    FavoriteNurseryItem(nursery)
                }
            }
        }
    }
}


@Composable
fun FavoriteNurseryItem(nursery: KinderInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(nursery.kindername, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            //Text("⭐ ${nursery.rating}")
            Text(nursery.addr, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FavoriteNurseriesScreenPreview() {
//    val dummyNurseries = listOf(
//        Nursery(
//            name = "푸른어린이집",
//            rating = 4.8,
//            address = "서울특별시 광진구 능동로 120",
//            phone = "02-1234-5678",
//            cctvCount = 5,
//            roomCount = 3,
//            playgroundCount = 1,
//            capacity = 80,
//            current = 60,
//            staffCount = 7,
//            hasBus = "Y",
//            reviewCount = 3
//        )
//    )
//    FavoriteNurseriesScreen(favorites = dummyNurseries)
//}
